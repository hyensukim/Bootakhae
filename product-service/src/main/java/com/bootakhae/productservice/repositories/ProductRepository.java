package com.bootakhae.productservice.repositories;

import com.bootakhae.productservice.entities.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByProductIdIn(List<String> productIds);
    Optional<ProductEntity> findByProductId(String productId);
    Optional<ProductEntity> findByNameAndProducer(String name, String producer);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductEntity  p where p.productId = :productId")
    Optional<ProductEntity> findByProductIdPessimistic(String productId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select p from ProductEntity  p where p.productId = :productId")
    Optional<ProductEntity> findByProductIdOptimistic(String productId);

    @Query("SELECT p FROM ProductEntity p WHERE p.eventTime IS NOT NULL AND p.eventTime BETWEEN :startDateTime AND :endDateTime")
    List<ProductEntity> findEventProductList(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
