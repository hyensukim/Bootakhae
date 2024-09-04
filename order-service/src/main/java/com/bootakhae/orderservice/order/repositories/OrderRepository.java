package com.bootakhae.orderservice.order.repositories;

import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderId(String orderId);

    Optional<OrderEntity> findByPayId(String payId);

    Page<OrderEntity> findByUserId(String userId, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.status = 'PAYMENT' AND dateDiff(o.updatedAt,now()) >= 1")
    List<OrderEntity> findAllAfterPayment();

    @Query("SELECT o FROM OrderEntity o WHERE o.status = 'SHIPPING' AND dateDiff(o.updatedAt,now()) >= 1")
    List<OrderEntity> findAllAfterShipping();

    @Query("SELECT o FROM OrderEntity o WHERE o.status = 'DONE' AND datediff(o.returnOrder.createdAt, now()) >= 1")
    List<OrderEntity> findAllForReturn();
}
