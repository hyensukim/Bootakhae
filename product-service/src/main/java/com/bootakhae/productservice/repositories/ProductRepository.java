package com.bootakhae.productservice.repositories;

import com.bootakhae.productservice.entities.ProductEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
        @QueryHint(name = "javax.persistence.lock.timeout", value = "10000")
    })
    List<ProductEntity> findAllByProductIdIn(List<String> productIds);

    Optional<ProductEntity> findByProductId(String productId);

    Optional<ProductEntity> findByNameAndProducer(String name, String producer);

    @Query("SELECT p FROM ProductEntity p WHERE p.isEventOpened = :isEvent")
    Page<ProductEntity> findByIsEvent(boolean isEvent, Pageable pageable);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductEntity  p where p.productId = :productId")
    Optional<ProductEntity> findByProductIdPessimistic(String productId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select p from ProductEntity  p where p.productId = :productId")
    Optional<ProductEntity> findByProductIdOptimistic(String productId);

    @Query("SELECT p FROM ProductEntity p WHERE p.eventTime IS NOT NULL AND p.eventTime BETWEEN :startDateTime AND :endDateTime")
    List<ProductEntity> findEventProductList(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
