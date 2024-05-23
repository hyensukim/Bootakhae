package com.bootakhae.orderservice.global.clients.vo.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RequestProduct {
    private String productId;
    private Long qty;
}
