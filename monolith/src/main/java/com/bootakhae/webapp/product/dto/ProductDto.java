package com.bootakhae.webapp.product.dto;

import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.product.vo.response.ResponseProduct;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String productId;

    private String name;

    private Long price;

    private Long stock;

    private String producer;

    private String nutritionFacts;

    public ProductEntity dtoToEntity() {
        return ProductEntity.builder()
                .productId(UUID.randomUUID().toString())
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .build();
    }

    public ResponseProduct dtoToVo(){
        return ResponseProduct.builder()
                .productId(this.productId)
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .build();
    }
}
