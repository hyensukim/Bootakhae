package com.bootakhae.orderservice.order.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PayDto {
    private String orderId;
    private String payId;
}
