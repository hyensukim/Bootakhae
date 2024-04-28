package com.bootakhae.productservice.repositories;

import com.bootakhae.productservice.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByProductIdIn(List<String> productIds);
    Optional<ProductEntity> findByProductId(String productId);
    Optional<ProductEntity> findByNameAndProducer(String name, String producer);
}
