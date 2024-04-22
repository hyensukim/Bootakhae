package com.bootakhae.webapp.order.repositories;

import com.bootakhae.webapp.order.entities.ReturnOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnOrderRepository extends JpaRepository<ReturnOrderEntity,Long> {
}
