package com.bootakhae.productservice.entities;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.global.constant.Function;
import com.bootakhae.productservice.global.constant.Type;
import com.bootakhae.productservice.global.entities.BaseEntity;
import com.bootakhae.productservice.global.exception.CustomException;
import com.bootakhae.productservice.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products", indexes = {
        @Index(name= "idx_name_producer", columnList = "product_name, product_producer")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductEntity extends BaseEntity {

//    @Version
//    private Long version; // 낙관적 락 - 버전 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name="product_id", nullable = false, unique=true, length = 50)
    private String productId; // uuid

    @Column(name="product_name", nullable = false, length = 50)
    private String name; // 상품명

    @Column(name="product_price", nullable = false)
    private Long price; // 상품가격

    @Column(name="product_stock", nullable = false)
    private Long stock; // 상품재고
    public void decreaseStock(Long qty){
        if(this.stock < qty){
            throw new CustomException(ErrorCode.LACK_PRODUCT_STOCK);
        }
        this.stock -= qty;
    }
    public void restoreStock(Long qty){
        this.stock += qty;
    }

    @Column(name="product_producer", nullable = false, length = 30)
    private String producer; // 제조사

    @Column(name="product_function", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Function function; // 상품 기능

    @Column(name="product_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Type type; // 상품 형태 - 캡슐, 음료(액상), 분말, 젤리, 스프레이형

    @Column(name="product_nutrition_facts", nullable = false)
    private String nutritionFacts; // 성분 - todo : 별도의 테이블 만들어보기

    @Column(name="prodcut_is_event_opened", nullable = false)
    private boolean isEventOpened;
    public void openThisEvent(){
        isEventOpened = true;
    }

    @Column(name="product_event_time")
    private LocalDateTime eventTime;
    public void registerEventTime(LocalDateTime eventTime){
        this.eventTime = eventTime;
    }

    /* entity -> dto */
    public ProductDto entityToDto(){
        return entityToDto(null);
    }

    public ProductDto entityToDto(Long qty) {
        return ProductDto.builder()
                .productId(this.productId)
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .qty(qty)
                .function(this.function.name())
                .type(this.type.name())
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .eventTime(this.eventTime)
                .build();
    }
}
