package com.bootakhae.orderservice.global.clients.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePay {
    private String payId;
    private String payMethod;
    private Long totalPrice;
}
