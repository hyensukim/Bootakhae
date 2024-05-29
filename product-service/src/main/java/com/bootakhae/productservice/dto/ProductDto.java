package com.bootakhae.productservice.dto;

import com.bootakhae.productservice.entities.ProductEntity;
import com.bootakhae.productservice.global.constant.Function;
import com.bootakhae.productservice.global.constant.Type;
import com.bootakhae.productservice.vo.response.ResponseProduct;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2305366629908850317L;

    private String productId;

    private String name;

    private Long price;

    private Long stock;

    private Long qty; // 재고 처리 후 반환 필드

    private String producer;

    private String function;

    private String type;

    private String nutritionFacts;

    private LocalDateTime eventTime;

    public ProductEntity dtoToEntity() {
        return ProductEntity.builder()
                .productId(UUID.randomUUID().toString())
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .function(Function.valueOf(this.function))
                .type(Type.valueOf(this.type))
                .nutritionFacts(this.nutritionFacts)
                .eventTime(this.eventTime)
                .build();
    }

    public ResponseProduct dtoToVo(){
        return ResponseProduct.builder()
                .productId(this.productId)
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .qty(this.qty)
                .function(this.function)
                .type(this.type)
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .eventTime(this.eventTime)
                .build();
    }
}
