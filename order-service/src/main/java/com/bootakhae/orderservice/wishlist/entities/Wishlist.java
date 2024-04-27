package com.bootakhae.orderservice.wishlist.entities;

import com.bootakhae.orderservice.order.vo.ProductInfo;
import com.bootakhae.orderservice.wishlist.dto.ResponseWishDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "wishlist", timeToLive = 60 * 60 * 24 * 30)
public class Wishlist {

    @Builder
    public Wishlist(
            String userId,
            String productId,
            Long qty
    ){
        seq = UUID.randomUUID().toString();
        this.userId = userId;
        this.productId = productId;
        this.qty = qty;
    }

    @Id
    private String seq;

    @Indexed
    private String userId;

    @Indexed
    private String productId;

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
