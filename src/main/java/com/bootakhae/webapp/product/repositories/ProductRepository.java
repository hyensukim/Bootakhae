package com.bootakhae.webapp.product.repositories;

import com.bootakhae.webapp.product.entities.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductId(String productId);
}
