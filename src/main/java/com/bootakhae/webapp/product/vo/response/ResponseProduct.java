package com.bootakhae.webapp.product.vo.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProduct {
    private String productId;

    private String name;

    private Integer price;

    private Integer stock;

    private String producer;

    private String nutritionFacts;
}
