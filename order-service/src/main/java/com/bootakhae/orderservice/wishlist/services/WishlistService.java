package com.bootakhae.orderservice.wishlist.services;

import com.bootakhae.orderservice.wishlist.dto.RequestWishDto;
import com.bootakhae.orderservice.wishlist.dto.ResponseWishDto;

public interface WishlistService {
    ResponseWishDto includeWish(RequestWishDto requestWishDto);

    ResponseWishDto updateQty(RequestWishDto requestWishDto);

    void excludeWish(String userId, String productId);

    ResponseWishDto getWishList(String userId, int nowPage, int pageSize);
}
