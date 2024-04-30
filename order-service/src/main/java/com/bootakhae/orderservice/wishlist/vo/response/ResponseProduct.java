package com.bootakhae.orderservice.wishlist.vo.response;

import lombok.*;

@Getter
@Builder
@ToString
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