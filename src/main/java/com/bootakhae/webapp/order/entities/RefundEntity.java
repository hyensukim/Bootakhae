package com.bootakhae.webapp.order.entities;

import com.bootakhae.webapp.entities.BaseEntity;
import jakarta.persistence.*;

@Entity
public class RefundEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "refund_reason")
    private String reason;
}
