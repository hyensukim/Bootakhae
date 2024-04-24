package com.bootakhae.productservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProduct {
    private String productId;

    private String name;

    private Long price;

    private Long stock;

    private String producer;

    private String nutritionFacts;
}
