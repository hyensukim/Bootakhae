package com.bootakhae.webapp.wishlist.vo;

import com.bootakhae.webapp.product.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    private String productId;
    private Long quantity;
}
