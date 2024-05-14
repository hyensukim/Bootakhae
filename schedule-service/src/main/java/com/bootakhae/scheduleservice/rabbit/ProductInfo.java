package com.bootakhae.scheduleservice.rabbit;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductInfo {
    private String productId;
    private Long qty;
}
