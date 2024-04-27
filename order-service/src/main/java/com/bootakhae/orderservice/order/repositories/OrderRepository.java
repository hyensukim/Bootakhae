package com.bootakhae.orderservice.order.repositories;

import com.bootakhae.orderservice.order.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
//    OrderEntity findByUserAndProduct(UserEntity user, ProductEntity product); - 이미 주문한 상품에 대한 처리
    Optional<OrderEntity> findByOrderId(String orderId);
    Page<OrderEntity> findByUserId(String userId, Pageable pageable);
}
