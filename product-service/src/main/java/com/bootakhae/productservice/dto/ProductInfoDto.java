package com.bootakhae.productservice.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductInfoDto {
    private String productId;
    private Long qty;
}
