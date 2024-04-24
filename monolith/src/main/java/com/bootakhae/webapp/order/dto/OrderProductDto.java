package com.bootakhae.webapp.order.dto;

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
    private Long quantity; // 주문수량
    private Long price;
    private Long stock; // 현재 상품 재고
}
