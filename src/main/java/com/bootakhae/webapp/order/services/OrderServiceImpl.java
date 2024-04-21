package com.bootakhae.webapp.order.services;

import com.bootakhae.webapp.order.dto.OrderDto;
import com.bootakhae.webapp.order.entities.OrderEntity;
import com.bootakhae.webapp.order.entities.OrderProduct;
import com.bootakhae.webapp.order.repositories.OrderProductRepository;
import com.bootakhae.webapp.order.repositories.OrderRepository;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.product.repositories.ProductRepository;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.repositories.UserRepository;

import com.bootakhae.webapp.wishlist.entities.WishEntity;
import com.bootakhae.webapp.wishlist.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
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

        List<OrderProduct> orderedProducts = new ArrayList<>();
        orderedProducts.add(orderProduct);

        return entityToDto(order, orderedProducts);
        
    }
    
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

        return entityToDto(order, orderedProductsToSave);
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
        return OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())
                .build();
    }

    private OrderDto entityToDto(OrderEntity order, List<OrderProduct> orderedProducts) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalPrice(order.getTotalPrice())
                .address1(order.getAddress1())
                .address2(order.getAddress2())
                .phone(order.getPhone())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getStatus())
                .orderedProducts(orderedProducts) // 여기서는 OrderProduct 의 DTO 변환 필요
                .build();
    }


    @Override
    public String deleteOrder(String orderId) {
        return "";
    }

    @Override
    public String getOrderDetails(String orderId) {
        return "";
    }

    @Override
    public String updateOrder(String orderId, OrderDto orderDetails) {
        return "";
    }

    @Override
    public String getOrderListByUserId(String userId) {
        return "";
    }
}
