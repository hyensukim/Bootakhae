package com.bootakhae.orderservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private String productId;
    private Long qty; // 주문수량
    private Long price;
}