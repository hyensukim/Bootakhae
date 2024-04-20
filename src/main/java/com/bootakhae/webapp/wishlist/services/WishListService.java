package com.bootakhae.webapp.wishlist.services;

import com.bootakhae.webapp.wishlist.dto.request.RequestWishDto;
import com.bootakhae.webapp.wishlist.dto.response.ResponseWishDto;

import java.util.List;

public interface WishListService {
    ResponseWishDto includeWish(RequestWishDto requestWishDto);

    ResponseWishDto updateQty(RequestWishDto requestWishDto);

    void excludeWish(String userId, String productId);

    List<ResponseWishDto> getWishList(int nowPage, int pageSize);
}
