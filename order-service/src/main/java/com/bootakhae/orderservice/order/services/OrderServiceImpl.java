package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.global.clients.FeignTemplate;
import com.bootakhae.orderservice.global.clients.vo.response.ResponsePay;
import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.global.exception.ServerException;
import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import com.bootakhae.orderservice.order.dto.PayDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.entities.OrderProduct;
import com.bootakhae.orderservice.order.entities.ReturnOrderEntity;
import com.bootakhae.orderservice.global.exception.CustomException;
import com.bootakhae.orderservice.global.exception.ErrorCode;
import com.bootakhae.orderservice.order.repositories.OrderRepository;
import com.bootakhae.orderservice.order.repositories.ReturnOrderRepository;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ReturnOrderRepository returnOrderRepository;

    private final FeignTemplate feignTemplate;

    @Transactional
    @Override
    public OrderDto registerOrder(OrderDto orderDetails) {
        log.debug("주문 등록 실행");
        long startTime = System.currentTimeMillis();
        List<OrderProductDto> orderProductList = orderDetails.getOrderProductList(); // 주문 요청 상품

        // 1. 재고 확인 및 감소
        List<ResponseProduct> productList = feignTemplate.checkAndDecreaseStock(orderProductList);

        try {
            // 2. 주문 처리
            OrderEntity order = orderDetails.dtoToEntity();
            long sum = 0L;
            for (ResponseProduct product : productList) {
                OrderProduct orderProduct =
                        OrderProduct.createOrderedProduct(order, product.getProductId(), product.getQty());
                order.getOrderProducts().add(orderProduct); // 주문 상품 등록
                sum += (product.getPrice() * product.getQty()); // 총비용 연산
            }

            orderRepository.save(order);
            log.debug("정상 처리까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            return order.entityToDto(sum);
        }catch(Exception e){
            feignTemplate.restoreStock(orderProductList);
            log.debug("복구까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            throw new ServerException(ErrorCode.FEIGN_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void completePayment(PayDto payDetails) {
        log.debug("결제 완료 변경");
        OrderEntity order = orderRepository.findByOrderId(payDetails.getOrderId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );
        order.registerPay(payDetails.getPayId());
        order.completePayment();
    }


    /**
     * 주문 취소
     * - Status_SHIPPING 되기 전
     * - 주문 후 주문 내역 삭제하지 않고 상태만 CANCEL 로 변경
     */
    @Transactional
    @Override
    public OrderDto cancelOrder(String orderId) {
        log.debug("주문 취소 실행");

        OrderEntity order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        if(order.getStatus() == Status.CANCEL){
            throw new CustomException(ErrorCode.ALREADY_CANCEL_ORDER);
        }
        else if(order.getStatus() == Status.PAYING){
            throw new CustomException(ErrorCode.NOT_COMPLETE_PAYMENT);
        }
        else {

            List<OrderProductDto> orderProductList = order.getOrderProducts().stream()
                    .map(OrderProduct::entityToDto)
                    .collect(Collectors.toList());

            if (order.getStatus() == Status.PAYMENT) {
                feignTemplate.restoreStock(orderProductList);
                order.cancelTheOrder();
            }

            return order.entityToDto();
        }
    }

    @Override
    public OrderDto getOrderDetails(String orderId) {
        log.debug("주문 상세 조회 실행");

        OrderEntity order = orderRepository.findByOrderId(orderId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        ResponsePay payDetails = feignTemplate.getOnePay(order.getPayId());

        return order.entityToDto(payDetails.getTotalPrice(), payDetails.getPayMethod());
    }

    @Override
    public List<OrderDto> getOrderListByUserId(String userId, int nowPage, int pageSize) {
        log.debug("회원의 주문 목록 조회 실행");
        PageRequest pageRequest = PageRequest.of(nowPage,pageSize, Sort.by("createdAt").descending());
        Page<OrderEntity> myOrderList = orderRepository.findByUserId(userId, pageRequest);
        return myOrderList.getContent()
                .stream()
                .map(OrderEntity::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void changeOrderStatusAfterPayment() {
        log.debug("배송중 이벤트 실행");
        List<OrderEntity> orderList = orderRepository.findAllAfterPayment();

        orderList.forEach(OrderEntity::startShipping);
    }

    @Transactional
    @Override
    public void changeOrderStatusAfterShipping() {
        log.debug("배송완료 이벤트 실행");
        List<OrderEntity> orderList = orderRepository.findAllAfterShipping();

        orderList.forEach(OrderEntity::completeShipping);
    }

    @Transactional
    @Override
    public void changeOrderStatusForReturn() {
        log.debug("반품 실행");
        List<OrderEntity> orderList = orderRepository.findAllForReturn();

        for(OrderEntity order : orderList){
            List<OrderProduct> orderProductList = order.getOrderProducts();

            List<OrderProductDto> orderproductList = orderProductList.stream()
                    .map(OrderProduct::entityToDto)
                    .collect(Collectors.toList());

            feignTemplate.restoreStock(orderproductList);
        }

        orderList.forEach(OrderEntity::returnTheOrder);
    }

    /**
     * 주문 반품
     * - 현재 주문상태 : 배송 완료
     * - 배송 완료로 바뀐 시점에서 1일 이내
     */
    @Transactional
    @Override
    public OrderDto returnOrderedProduct(ReturnOrderDto returnOrderDetails) {
        log.debug("주문에 포함된 모든 상품 반품 실행");

        OrderEntity order = orderRepository.findByOrderId(returnOrderDetails.getOrderId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        if(order.getStatus() != Status.DONE){
            throw new CustomException(ErrorCode.NOT_DONE_STATUS);
        }

        if(Math.abs(Duration.between(order.getUpdatedAt(),LocalDateTime.now()).toDays()) > 1){
            throw new CustomException(ErrorCode.NOT_RETURN_DURATION);
        }

        ReturnOrderEntity returnOrder = new ReturnOrderEntity();
        returnOrder.writeReason(returnOrderDetails.getReason());
        returnOrder = returnOrderRepository.save(returnOrder);

        order.returnOrder(returnOrder);

        return order.entityToDto();
    }
}
