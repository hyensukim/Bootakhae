package com.bootakhae.webapp.product.entities;

import com.bootakhae.webapp.entities.BaseEntity;
import com.bootakhae.webapp.product.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name="product_id", nullable = false, unique=true, length = 50)
    private String productId;

    @Column(name="product_name", nullable = false, unique=true, length = 50)
    private String name;

    @Column(name="product_price", nullable = false)
    private Long price;

    @Column(name="product_stock", nullable = false)
    private Long stock;

    public void takeStock(long qty){
        stock += qty;
    }

    public void deductStock(long qty){
        stock -= qty;
    }

    @Column(name="product_producer", nullable = false, length = 30)
    private String producer;

    @Column(name="product_nutrition_facts", nullable = false)
    private String nutritionFacts;

    public ProductDto entityToDto(){
        return ProductDto.builder()
                .productId(this.productId)
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .build();
    }
}
