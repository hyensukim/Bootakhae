package com.bootakhae.payservice.repositories;

import com.bootakhae.payservice.entities.PayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRepository extends JpaRepository<PayEntity, Long> {
    Optional<PayEntity> findByPayId(String payId);
    boolean existsPayEntityByOrderId (String orderId);
}
