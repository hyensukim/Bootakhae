package com.bootakhae.orderservice.global.clients.vo.response;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProduct {
    private String productId;
    private Long price;
    private Long qty;
}