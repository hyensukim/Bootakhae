package com.bootakhae.scheduleservice.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOrder {
    private String orderId;
    private String userId;
    private String payId;
    private String payMethod;
    private Long totalPrice;
    private String address1;
    private String address2;
    private String phone;
    private LocalDateTime returnAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String orderStatus;
}

