package com.bootakhae.productservice.entities;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products", indexes = {
        @Index(name= "idx_name_producer", columnList = "product_name, product_producer")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity {

//    @Version
//    private Long version; // 낙관적 락 - 버전 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name="product_id", nullable = false, unique=true, length = 50)
    private String productId;

    @Column(name="product_name", nullable = false, length = 50)
    private String name;

    @Column(name="product_price", nullable = false)
    private Long price;

    @Column(name="product_stock", nullable = false)
    private Long stock;
    public void decreaseStock(Long qty){
        this.stock -= qty;
    }
    public void restoreStock(Long qty){
        this.stock += qty;
    }

    @Column(name="product_producer", nullable = false, length = 30)
    private String producer;

    @Column(name="product_nutrition_facts", nullable = false)
    private String nutritionFacts;

    @Column(name="prodcut_is_event_opend", nullable = false)
    private boolean isEventOpened;
    public void openThisEvent(){
        isEventOpened = true;
    }

    @Column(name="product_event_time")
    private LocalDateTime eventTime;
    public void registerEventTime(LocalDateTime eventTime){
        this.eventTime = eventTime;
    }

    public ProductDto entityToDto(){
        return ProductDto.builder()
                .productId(this.productId)
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .eventTime(this.eventTime)
                .build();
    }
}
