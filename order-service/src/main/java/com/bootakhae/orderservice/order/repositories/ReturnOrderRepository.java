package com.bootakhae.orderservice.order.repositories;

import com.bootakhae.orderservice.order.entities.ReturnOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnOrderRepository extends JpaRepository<ReturnOrderEntity,Long> {
}
