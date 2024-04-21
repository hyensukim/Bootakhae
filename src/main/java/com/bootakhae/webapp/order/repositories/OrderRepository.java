package com.bootakhae.webapp.order.repositories;

import com.bootakhae.webapp.order.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
//    OrderEntity findByUserAndProduct(UserEntity user, ProductEntity product); - 이미 주문한 상품에 대한 처리
}
