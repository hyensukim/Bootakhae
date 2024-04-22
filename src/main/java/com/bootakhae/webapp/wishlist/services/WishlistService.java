package com.bootakhae.webapp.wishlist.services;

import com.bootakhae.webapp.wishlist.dto.request.RequestWishDto;
import com.bootakhae.webapp.wishlist.dto.response.ResponseWishDto;

public interface WishlistService {
    ResponseWishDto includeWish(RequestWishDto requestWishDto);

    ResponseWishDto updateQty(RequestWishDto requestWishDto);

    void excludeWish(String userId, String productId);

    ResponseWishDto getWishList(String userId, int nowPage, int pageSize);
}
