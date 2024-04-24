package com.bootakhae.orderservice.services;

import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.dto.OrderDto;
import com.bootakhae.orderservice.dto.OrderProductDto;
import com.bootakhae.orderservice.dto.ReturnOrderDto;
import com.bootakhae.orderservice.entities.OrderEntity;
import com.bootakhae.orderservice.entities.OrderProduct;
import com.bootakhae.orderservice.entities.ReturnOrderEntity;
import com.bootakhae.orderservice.repositories.OrderProductRepository;
import com.bootakhae.orderservice.repositories.OrderRepository;
import com.bootakhae.orderservice.repositories.ReturnOrderRepository;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.product.repositories.ProductRepository;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.repositories.UserRepository;
import com.bootakhae.webapp.wishlist.entities.WishEntity;
import com.bootakhae.webapp.wishlist.repositories.WishlistRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ReturnOrderRepository returnOrderRepository;
    private final WishlistRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public OrderDto registerOrder(OrderDto orderDetails) {
        log.debug("주문 등록 실행");

        UserEntity user = userRepository.findByUserId(orderDetails.getUserId()).orElseThrow(
                () -> new RuntimeException("주문 등록 : 로그인 후 이용 바랍니다.")
        );

        ProductEntity product = productRepository.findByProductId(orderDetails.getProductId()).orElseThrow(
                () -> new RuntimeException("주문 등록 : 등록되지 않은 상품입니다.")
        );

        orderProductRepository.findByOrderUserAndProduct(user, product).ifPresent(op->{
            throw new RuntimeException("주문 등록 : 이미 주문한 상품입니다.");
        });

        OrderEntity order = createOrder(orderDetails, user, product.getPrice());
        orderRepository.save(order);

        OrderProduct orderProduct = createOrderedProduct(order, product, orderDetails.getQuantity());
        orderProductRepository.save(orderProduct);

        return entityToDto(order, orderProduct.entityToDto());
        
    }

    @Transactional
    @Override
    public OrderDto registerOrders(OrderDto orderDetails) {
        log.debug("위시리스트 주문 등록 실행");

        UserEntity user = userRepository.findByUserId(orderDetails.getUserId()).orElseThrow(
                () -> new RuntimeException("주문 등록 : 로그인 후 이용 바랍니다.")
        );

        List<WishEntity> wishList = wishListRepository.findAllByUser(user); // 위시 리스트 조회

        if(wishList.isEmpty()) {
            throw new RuntimeException("위시리스트 주문 등록 : 상품 등록 후 이용 바랍니다.");
        }

        OrderEntity order = createOrder(orderDetails, user);
        List<OrderProduct> orderedProductsToSave = new ArrayList<>();

        long sum = 0L;
        for(WishEntity wish : wishList){
            ProductEntity product = wish.getProduct();
            sum += wish.getQuantity() * product.getPrice();

            // 주문 상품 생성, 각 상품의 실제 수량 반영
            OrderProduct orderProduct = createOrderedProduct(order, product, wish.getQuantity());
            orderedProductsToSave.add(orderProduct);
        }
        // 전체 주문 가격 계산
        order.calculateTotalPrice(sum);

        // 주문 정보 저장
        order = orderRepository.save(order);

        // 주문 상품 정보 일괄 저장
        orderProductRepository.saveAll(orderedProductsToSave);

        // 주문 상품 Dto 에 담기
        List<OrderProductDto> orderedProducts = orderedProductsToSave.stream().map(OrderProduct::entityToDto).toList();

        return entityToDto(order, orderedProducts);
    }

    private OrderEntity createOrder(OrderDto orderDetails, UserEntity user){
        return OrderEntity.builder()
                .user(user)
                .address1(orderDetails.getAddress1())
                .address2(orderDetails.getAddress2())
                .phone(orderDetails.getPhone())
                .build();
    }

    private OrderEntity createOrder(OrderDto orderDetails, UserEntity user, Long price) {
        Long totalPrice = price * orderDetails.getQuantity();
        return OrderEntity.builder()
                .user(user)
                .address1(orderDetails.getAddress1())
                .address2(orderDetails.getAddress2())
                .phone(orderDetails.getPhone())
                .totalPrice(totalPrice)
                .build();
    }

    private OrderProduct createOrderedProduct(OrderEntity order, ProductEntity product, Long quantity) {

        if(product.getStock() >= quantity){
            product.deductStock(quantity);
        }
        else{
            throw new RuntimeException("재고가 부족합니다.");
        }

        return OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())
                .build();
    }

    private OrderDto entityToDto(OrderEntity order, OrderProductDto orderProduct) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalPrice(order.getTotalPrice())
                .address1(order.getAddress1())
                .address2(order.getAddress2())
                .phone(order.getPhone())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getStatus())
                .orderProduct(orderProduct)
                .build();
    }

    private OrderDto entityToDto(OrderEntity order, List<OrderProductDto> orderedProducts) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalPrice(order.getTotalPrice())
                .address1(order.getAddress1())
                .address2(order.getAddress2())
                .phone(order.getPhone())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getStatus())
                .orderedProducts(orderedProducts)
                .build();
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
                ()->new RuntimeException("주문 취소 : 존재하지 않는 주문입니다.")
        );

        OrderProduct orderProduct = orderProductRepository.findByOrder(order).orElseThrow(
                ()->new RuntimeException("주문 취소 : 주문하지 않은 상품입니다.")
        );

        if(order.getStatus() == Status.PAYMENT){
            orderProduct.getProduct().takeStock(orderProduct.getQuantity());
            order.cancelTheOrder();
        }

        return entityToDto(order,orderProduct.entityToDto());
    }

    @Override
    public OrderDto getOrderDetails(String orderId) {
        log.debug("주문 상세 조회 실행");

        OrderEntity order = orderRepository.findByOrderId(orderId).orElseThrow(
                ()->new RuntimeException("주문 상세 조회 : 존재하지 않는 주문입니다.")
        );

        return order.entityToDto();
    }

    @Override
    public List<OrderDto> getOrderListByUserId(String userId, int nowPage, int pageSize) {
        log.debug("회원의 주문 목록 조회 실행");
        PageRequest pageRequest = PageRequest.of(nowPage,pageSize, Sort.by("createdAt").descending());
        Page<OrderEntity> myOrderList = orderRepository.findByUserUserId(userId, pageRequest);
        return myOrderList.getContent().stream().map(OrderEntity::entityToDto).toList();
    }

    /**
     * 주문 반품
     * - 현재 주문상태 : 배송 완료
     * - 배송 완료로 바뀐 시점에서 1일 이내
     */
    @Transactional
    @Override
    public OrderDto returnOrderedProduct(String orderId, ReturnOrderDto returnOrderDetails) {
        log.debug("주문에 포함된 모든 상품 반품 실행");

        OrderEntity order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new RuntimeException("주문 반품 : 주문되지 않은 내역입니다.")
        );

        if(order.getStatus() != Status.DONE){
            throw new RuntimeException("주문 반품 : 현재 배송이 완료되지 않았습니다. 배송 완료 후 반품 요청 바랍니다.");
        }

        if(Math.abs(Duration.between(order.getUpdatedAt(),LocalDateTime.now()).toDays()) > 1){
            throw new RuntimeException("주문 반품 : 배송 완료 후 1일이 지나 반품이 불가합니다.");
        }

        ReturnOrderEntity returnOrder = new ReturnOrderEntity();
        returnOrder.writeReason(returnOrderDetails.getReason());

        returnOrder = returnOrderRepository.save(returnOrder);

        order.returnOrder(returnOrder);

        return order.entityToDto();
    }

    @Scheduled(cron = "${schedule.cron}")
    @Transactional
    @Override
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
                        (op) ->{op.getProduct().takeStock(op.getQuantity());}
                );
            }
        }
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderDto orderDetails) {
        return null;
    }
}
