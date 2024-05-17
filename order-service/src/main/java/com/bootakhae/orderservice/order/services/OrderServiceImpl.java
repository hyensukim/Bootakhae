package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.global.clients.FeignTemplate;
import com.bootakhae.orderservice.global.clients.vo.request.RequestPay;
import com.bootakhae.orderservice.global.clients.vo.request.RequestStock;
import com.bootakhae.orderservice.global.clients.vo.response.ResponsePay;
import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.global.constant.StockProcess;
import com.bootakhae.orderservice.global.exception.ClientException;
import com.bootakhae.orderservice.global.exception.ServerException;
import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.entities.OrderProduct;
import com.bootakhae.orderservice.order.entities.ReturnOrderEntity;
import com.bootakhae.orderservice.global.exception.CustomException;
import com.bootakhae.orderservice.global.exception.ErrorCode;
import com.bootakhae.orderservice.order.repositories.OrderRepository;
import com.bootakhae.orderservice.order.repositories.ReturnOrderRepository;
import com.bootakhae.orderservice.order.vo.ProductInfo;
import com.bootakhae.orderservice.wishlist.entities.Wishlist;
import com.bootakhae.orderservice.wishlist.repositories.WishlistRepository;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseUser;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import io.github.resilience4j.retry.annotation.Retry;
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
    private final WishlistRepository wishlistRepository;

    private final FeignTemplate feignTemplate;

    @Transactional
    @Override
    @CircuitBreaker(name = "default-CB")
    @Retry(name = "default-RT")
    public OrderDto registerOrder(OrderDto orderDetails) {
        log.debug("주문 등록 실행");

        long startTime = System.currentTimeMillis();

        ResponseUser user = feignTemplate.findUserByUserId(orderDetails.getUserId());

        ResponseProduct product = updateStock(
                StockProcess.DECREASE,
                orderDetails.getProductId(),
                orderDetails.getQty()
        );

        try {
            OrderEntity order = orderDetails.dtoToEntity(user.getUserId(), product.getPrice());
            OrderProduct orderProduct = OrderProduct.createOrderedProduct(order, product, orderDetails.getQty());
            order.getOrderProducts().add(orderProduct);

            log.debug("결제 등록 실행");
            ResponsePay pay = feignTemplate.registerPay(RequestPay.builder()
                    .orderId(order.getOrderId())
                    .payMethod(orderDetails.getPayMethod())
                    .totalPrice(product.getPrice() * orderProduct.getQty())
                    .build()
            );
            order.registerPay(pay.getPayId());
            orderRepository.save(order);
            log.debug("정상 처리까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            return order.entityToDto(pay.getTotalPrice(), pay.getPayMethod());
        }catch(ClientException e){
            ResponseProduct response = updateStock(
                    StockProcess.RESTORE,
                    orderDetails.getProductId(),
                    orderDetails.getQty()
            );

            log.debug("재고 수량 복구 성공");
            log.debug("{}의 재고 : {}", response.getName(), response.getStock());
            log.debug("복구까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            throw new ClientException(ErrorCode.FEIGN_CLIENT_ERROR, e.getMessage());
        }catch(Exception e){
            ResponseProduct response = updateStock(
                    StockProcess.RESTORE,
                    orderDetails.getProductId(),
                    orderDetails.getQty()
            );

            log.debug("재고 수량 복구 성공");
            log.debug("{}의 재고 : {}", response.getName(), response.getStock());
            log.debug("복구까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            throw new ServerException(ErrorCode.FEIGN_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "default-CB")
    public OrderDto registerWishlist(OrderDto orderDetails) {
        log.debug("위시리스트 주문 등록 실행");

        long startTime = System.currentTimeMillis();

        ResponseUser user =  feignTemplate.findUserByUserId(orderDetails.getUserId());
        List<Wishlist> wishlist = wishlistRepository.findAllByUserId(user.getUserId());
        if(wishlist.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXISTS_WISHLIST);
        }

        Map<String, ResponseProduct> productsMap = new HashMap<>();
        List<ProductInfo> productInfoList = wishlist.stream()
                .map(wish -> ProductInfo.builder()
                        .productId(wish.getProductId())
                        .qty(wish.getQty())
                        .build())
                .collect(Collectors.toList());
        log.debug("위시리스트 관련 재고 감소 실행");
        List<ResponseProduct> productList =  feignTemplate.updateStock(
                RequestStock.builder()
                        .stockProcess(StockProcess.DECREASE.name())
                        .productInfoList(productInfoList)
                        .build()
        );

        for (ResponseProduct product : productList) {
            productsMap.put(product.getProductId(), product);
        }

        OrderEntity order = orderDetails.dtoToEntity(user.getUserId());
        try{

            List<OrderProduct> orderProductList = order.getOrderProducts();

            long sum = 0L;

            for (Wishlist wish : wishlist) {
                ResponseProduct product = productsMap.get(wish.getProductId());

                OrderProduct orderProduct = OrderProduct.createOrderedProduct(order, product, wish.getQty());

                sum += product.getPrice() * orderProduct.getQty();

                orderProductList.add(orderProduct);
            }

            // 결제 생성
            ResponsePay pay = feignTemplate.registerPay(RequestPay.builder()
                    .orderId(order.getOrderId())
                    .payMethod(orderDetails.getPayMethod())
                    .totalPrice(sum)
                    .build()
            );

            order.registerPay(pay.getPayId());

            order = orderRepository.save(order);

            wishlistRepository.deleteAll(wishlist); // 위시리스트 비우기

            log.debug("정상 처리까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);

            return order.entityToDto(pay.getTotalPrice(), pay.getPayMethod());

        }catch(ClientException e){
            List<ResponseProduct> responseList = feignTemplate.updateStock(
                    RequestStock.builder()
                            .stockProcess(StockProcess.RESTORE.name())
                            .productInfoList(productInfoList)
                            .build()
            );
            log.debug("위시리스트 재고 수량 복구 성공");
            log.debug("재고 수량 복구 성공 갯수 : {}", responseList.size());
            log.debug("복구까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            throw new ClientException(ErrorCode.FEIGN_CLIENT_ERROR, e.getMessage());
        }catch(Exception e){
            List<ResponseProduct> responseList = feignTemplate.updateStock(
                    RequestStock.builder()
                            .stockProcess(StockProcess.RESTORE.name())
                            .productInfoList(productInfoList)
                            .build()
            );
            log.debug("위시리스트 재고 수량 복구 성공");
            log.debug("재고 수량 복구 성공 갯수 : {}", responseList.size());
            log.debug("복구까지 걸리는 시간 : {}", System.currentTimeMillis() - startTime);
            throw new ServerException(ErrorCode.FEIGN_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    @Override
    public OrderDto completePayment(String payId) {
        OrderEntity order = orderRepository.findByPayId(payId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        order.completePayment();

        return order.entityToDto();
    }


    /**
     * 주문 취소
     * - Status_SHIPPING 되기 전
     * - 주문 후 주문 내역 삭제하지 않고 상태만 CANCEL 로 변경
     */
    @Transactional
    @Override
    public OrderDto removeOrder(String orderId) {
        log.debug("주문 삭제 실행");

        OrderEntity order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        List<OrderProduct> orderProducts = order.getOrderProducts();

        List<ProductInfo> productInfoList = orderProducts.stream()
                .map(wish -> ProductInfo.builder()
                        .productId(wish.getProductId())
                        .qty(wish.getQty())
                        .build())
                .collect(Collectors.toList());

        if(!orderProducts.isEmpty() && order.getStatus() == Status.PAYMENT){
            feignTemplate.updateStock(
                    RequestStock.builder()
                            .stockProcess(StockProcess.RESTORE.name())
                            .productInfoList(productInfoList)
                            .build()
                    );
            order.cancelTheOrder();
        }
        else if(order.getStatus() == Status.CANCEL){
            throw new CustomException(ErrorCode.ALREADY_CANCEL_ORDER);
        }

        return order.entityToDto();
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
    public List<OrderDto> getAllOrders() {
        List<OrderEntity> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(OrderEntity::entityToDto)
                .collect(Collectors.toList());
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
        log.debug("반품 이벤트 실행");
        List<OrderEntity> orderList = orderRepository.findAllForReturn();

        for(OrderEntity order : orderList){
            List<OrderProduct> orderProductList = order.getOrderProducts();

            List<ProductInfo> productInfoList = orderProductList.stream()
                    .map(o-> new ProductInfo(o.getProductId(),o.getQty()))
                    .collect(Collectors.toList());

            feignTemplate.updateStock(RequestStock.builder()
                    .stockProcess(StockProcess.RESTORE.name())
                    .productInfoList(productInfoList)
                    .build());
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

    private ResponseProduct updateStock(StockProcess stockProcess, String productId, Long qty) {
        List<ResponseProduct> productList =  feignTemplate.updateStock(
                RequestStock.builder()
                        .productInfoList(List.of(ProductInfo.builder()
                                .productId(productId)
                                .qty(qty)
                                .build()))
                        .stockProcess(stockProcess.name())
                        .build()
        );

        return productList.stream().findFirst().orElseThrow(
                () -> new CustomException(ErrorCode.NOT_REGISTERED_PRODUCT)
        );
    }
}
