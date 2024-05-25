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

    private String payMethod;

    private Long totalPrice;

    private LocalDateTime createdAt;
}
