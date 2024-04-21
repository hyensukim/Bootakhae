package com.bootakhae.webapp.order.repositories;

import com.bootakhae.webapp.order.entities.OrderProduct;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
    Optional<OrderProduct> findByOrderUserAndProduct(UserEntity user, ProductEntity product);
}
