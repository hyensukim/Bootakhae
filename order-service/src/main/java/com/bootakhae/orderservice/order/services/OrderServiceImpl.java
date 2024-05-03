package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.global.clients.ProductClient;
import com.bootakhae.orderservice.global.clients.UserClient;
import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.entities.OrderProduct;
import com.bootakhae.orderservice.order.entities.ReturnOrderEntity;
import com.bootakhae.orderservice.global.exception.CustomException;
import com.bootakhae.orderservice.global.exception.ErrorCode;
import com.bootakhae.orderservice.order.repositories.OrderProductRepository;
import com.bootakhae.orderservice.order.repositories.OrderRepository;
import com.bootakhae.orderservice.order.repositories.ReturnOrderRepository;
import com.bootakhae.orderservice.wishlist.entities.Wishlist;
import com.bootakhae.orderservice.wishlist.repositories.WishlistRepository;
import com.bootakhae.orderservice.wishlist.vo.response.ResponseProduct;
import com.bootakhae.orderservice.wishlist.vo.response.ResponseUser;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final OrderProductRepository orderProductRepository;
    private final ReturnOrderRepository returnOrderRepository;
    private final WishlistRepository wishListRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    @Transactional
    @Override
    public OrderDto registerOrder(OrderDto orderDetails) {
        log.debug("주문 등록 실행");

        ResponseUser user = findUserByUserId(orderDetails.getUserId());
        ResponseProduct product = findProductByProductId(orderDetails.getProductId());

        OrderEntity order = orderDetails.dtoToEntity(user.getResUserId(), product.getPrice());

        OrderProduct orderProduct = createOrderedProduct(order, product, orderDetails.getQty());

        order.getOrderProducts().add(orderProduct);

        orderRepository.save(order);

        return order.entityToDto();
    }

    @Transactional
    @Override
    public OrderDto registerWishlist(OrderDto orderDetails) {
        log.debug("위시리스트 주문 등록 실행");

        ResponseUser user = findUserByUserId(orderDetails.getUserId());

        List<Wishlist> wishlist = wishListRepository.findAllByUserId(user.getResUserId());
        if(wishlist.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXISTS_WISHLIST);
        }

        // 상품 map
        Map<String, ResponseProduct> productsMap = new HashMap<>();
        List<String> productIds = wishlist.stream().map(Wishlist::getProductId).collect(Collectors.toList());
        List<ResponseProduct> productList = findProductsByProductIds(productIds);
        for (ResponseProduct product : productList) {
            productsMap.put(product.getProductId(), product);
        }

        // 주문 생성
        OrderEntity order = orderDetails.dtoToEntity(user.getResUserId());
        List<OrderProduct> orderProductList = order.getOrderProducts();

        // 위시리스트 항목에 대한 주문 상품 생성 및 가격 계산
        for (Wishlist wish : wishlist) {
            ResponseProduct product = productsMap.get(wish.getProductId());

            order.calculateTotalPrice(product.getPrice() * wish.getQty());

            OrderProduct orderProduct = createOrderedProduct(order, product, wish.getQty());
            orderProductList.add(orderProduct);
        }

        order = orderRepository.save(order);

        return order.entityToDto();
    }

    /**
     * 주문된 상품 엔티티 생성
     */
    private OrderProduct createOrderedProduct(OrderEntity order, ResponseProduct product, Long qty) {

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .productId(product.getProductId())
                .productName(product.getName()) // 결합력을 낮추기 위해 저장
                .productStock(product.getStock()) // 결합력을 낮추기 위해 저장
                .price(product.getPrice())
                .qty(qty)
                .build();

        // 재고 반영 + 재고 동기화
        if( orderProduct.getProductStock() >= qty){
            long stock = orderProduct.decreaseStock(qty);
            updateStock(orderProduct.getProductId(), stock);
        }
        else{
            throw new CustomException(ErrorCode.LACK_PRODUCT_STOCK);
        }

        return orderProduct;
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
                ()->new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        if(order.getStatus() == Status.PAYMENT){
            List<OrderProduct> orderProducts = order.getOrderProducts();
            for(OrderProduct orderProduct : orderProducts){
                long stock = orderProduct.restoreStock(orderProduct.getQty());
                updateStock(orderProduct.getProductId(), stock);
            }
            order.cancelTheOrder();
        }

        return order.entityToDto();
    }

    @Override
    public OrderDto getOrderDetails(String orderId) {
        log.debug("주문 상세 조회 실행");

        OrderEntity order = orderRepository.findByOrderId(orderId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_EXISTS_ORDER)
        );

        return order.entityToDto();
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

    @Scheduled(cron = "${schedule.cron}")
    @Transactional
    public void changeOrderStatus() {
        log.info("주문 상태 업데이트 실행");

        List<OrderEntity> orderList = orderRepository.findAll();
        for(OrderEntity order : orderList){
            if(order.getStatus() == Status.PAYMENT
                    && Math.abs(Duration.between(order.getCreatedAt(),LocalDateTime.now()).toDays()) >= 1){
                order.startShipping();
            }
            else if(order.getStatus() == Status.SHIPPING
                    && Math.abs(Duration.between(order.getUpdatedAt(),LocalDateTime.now()).toDays()) >= 1){
                order.completeShipping();
            }
            else if(order.getStatus() != Status.RETURN
                    && order.getReturnOrder() != null
                    && Math.abs(Duration.between(order.getReturnOrder().getCreatedAt(),LocalDateTime.now())
                    .toDays()) >= 1){
                order.returnTheOrder();
                orderProductRepository.findByOrder(order).ifPresent(
                    // fixme 이벤트 발생하여 재고 일치화 해주도록 개선!! - 이벤트 브로커
                    (op) ->{
                        Long stock = op.restoreStock(op.getQty());
                        updateStock(op.getProductId(), stock);
                    }
                );
            }
        }
    }

    /**
     * 회원 정보 조회
     */
    @Retry(name = "default-RT")
    private ResponseUser findUserByUserId(String userId) {
        return userClient.getUser(userId);
    }

    /**
     * 상품 정보 조회
     */
    @Retry(name = "default-RT")
    private ResponseProduct findProductByProductId(String productId) {
        return productClient.getOneProduct(productId);
    }

    /**
     * 상품 정보 일괄 조회
     */
    @Retry(name = "default-RT")
    private List<ResponseProduct> findProductsByProductIds(List<String> productIds) {
        return productClient.getProducts(productIds);
    }

    /**
     * 재고 업데이트
     */
    // fixme 이벤트 발생하여 재고 일치화 해주도록 개선!! - 이벤트 브로커
    @Retry(name = "default-RT")
    private void updateStock(String productId, Long stock) {
        ResponseProduct response = productClient.updateStock(productId, stock);
        log.debug("{} 재고 : {}", response.getName(), response.getStock());
    }
}
