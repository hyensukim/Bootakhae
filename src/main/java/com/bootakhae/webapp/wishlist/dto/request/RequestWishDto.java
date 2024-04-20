package com.bootakhae.webapp.wishlist.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestWishDto {
    private String productId;

    private String userId;

    private Integer quantity;

}
