package com.bootakhae.orderservice.global.rabbit;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderInfo implements Serializable {
    private String orderId;
    private String afterStatus;
}
