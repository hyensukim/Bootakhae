package com.bootakhae.orderservice.wishlist.entities;

import com.bootakhae.orderservice.order.vo.ProductInfo;
import com.bootakhae.orderservice.wishlist.dto.ResponseWishDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@RedisHash(value = "wishlist", timeToLive = 60 * 60 * 24 * 30)
@Table(name="wishlist", indexes = {
        @Index(name = "idx_name_wishlist", columnList = "user_id, product_id")
})
@Entity
public class Wishlist {

    @Builder
    public Wishlist(
            String userId,
            String productId,
            Long qty
    ){
        this.userId = userId;
        this.productId = productId;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    private String userId;

    @Column(name = "product_id", unique = true, nullable = false, length = 50)
    private String productId;

    @Column(name = "wishlist_qty", nullable = false)
    private Long qty;

    public void changeQty(Long qty) {
        this.qty = qty;
    }

    public ResponseWishDto entityToDto(){
        return ResponseWishDto.builder()
                .userId(this.userId)
                .productInfo(entityToVo())
                .build();
    }

    public ProductInfo entityToVo(){
        return ProductInfo.builder()
                .productId(this.productId)
                .qty(this.qty)
                .build();
    }
}
