package com.bootakhae.orderservice.repositories;

import com.bootakhae.orderservice.entities.ReturnOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnOrderRepository extends JpaRepository<ReturnOrderEntity,Long> {
}
