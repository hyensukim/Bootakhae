package com.bootakhae.orderservice.order.repositories;

import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
    Optional<OrderProduct> findByOrder(OrderEntity order);
}
