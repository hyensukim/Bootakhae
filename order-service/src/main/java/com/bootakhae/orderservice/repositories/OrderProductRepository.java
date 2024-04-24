package com.bootakhae.orderservice.repositories;

import com.bootakhae.orderservice.entities.OrderEntity;
import com.bootakhae.orderservice.entities.OrderProduct;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
    Optional<OrderProduct> findByOrder(OrderEntity order);
    Optional<OrderProduct> findByOrderUserAndProduct(UserEntity user, ProductEntity product);
}
