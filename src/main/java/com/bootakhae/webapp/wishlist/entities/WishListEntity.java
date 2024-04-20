package com.bootakhae.webapp.wishlist.entities;

import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.wishlist.dto.response.ResponseWishDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wish_list", indexes = {@Index(name="index_product_user", columnList = "product_id,user_id")})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WishListEntity {

    @Builder
    public WishListEntity(ProductEntity product, UserEntity user, Integer quantity) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue
    private Long seq;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name="wish_qty")
    private Integer quantity = 0;

    public void changeQty(Integer qty){
        this.quantity = qty;
    }

    public ResponseWishDto entityToDto(){
        return ResponseWishDto.builder()
                .product(this.product.getProductId())
                .user(this.user.getUserId())
                .quantity(this.quantity)
                .build();
    }
}
