package com.bootakhae.payservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePay {
    private String payId;

    private String orderId;

    private String orderStatus;

    private String payMethod;

    private String status;

    private Long totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
