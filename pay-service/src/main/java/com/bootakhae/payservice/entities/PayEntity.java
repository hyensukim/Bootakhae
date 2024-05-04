package com.bootakhae.payservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String payId;

    private String paySystem;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
