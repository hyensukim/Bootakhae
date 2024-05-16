package com.bootakhae.orderservice.wishlist.services;

import com.bootakhae.orderservice.global.clients.FeignTemplate;
import com.bootakhae.orderservice.global.clients.ProductClient;
import com.bootakhae.orderservice.global.clients.UserClient;
import com.bootakhae.orderservice.global.exception.CustomException;
import com.bootakhae.orderservice.global.exception.ErrorCode;
import com.bootakhae.orderservice.order.vo.ProductInfo;

import com.bootakhae.orderservice.wishlist.dto.RequestWishDto;
import com.bootakhae.orderservice.wishlist.dto.ResponseWishDto;
import com.bootakhae.orderservice.wishlist.entities.Wishlist;
import com.bootakhae.orderservice.wishlist.repositories.WishlistRepository;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseUser;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final FeignTemplate feignTemplate;
    private final WishlistRepository wishlistRepository;

    @Transactional
    @Override
    public ResponseWishDto includeWish(RequestWishDto wishDetailsList) {
        log.debug("위시 리스트 등록 실행");

        ResponseUser user = feignTemplate.findUserByUserId(wishDetailsList.getUserId());

        wishlistRepository.findByUserIdAndProductId(user.getUserId(), wishDetailsList.getProductId()).ifPresent(
                wlist -> {throw new CustomException(ErrorCode.DUPLICATE_WISHLIST);}
        );

        Wishlist wishlist = wishlistRepository.save(Wishlist.builder()
                .userId(user.getUserId())
                .productId(wishDetailsList.getProductId())
                .qty(wishDetailsList.getQty())
                .build()
        );

        return wishlist.entityToDto();
    }

    @Transactional
    @Override
    public ResponseWishDto updateQty(RequestWishDto wishDetailsList) {
        log.debug("위시 리스트 수량 변경 실행");
        ResponseUser user = feignTemplate.findUserByUserId(wishDetailsList.getUserId());

        Wishlist wishlist = wishlistRepository
                .findByUserIdAndProductId(user.getUserId(), wishDetailsList.getProductId())
                .map(ws -> {
                    ws.changeQty(wishDetailsList.getQty());
                    return wishlistRepository.save(ws);
                }).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_WISHLIST));
        return wishlist.entityToDto();
    }

    @Transactional
    @Override
    public void excludeWish(String userId, String productId) {
        log.debug("위시 리스트 삭제 실행");

        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId, productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_WISHLIST)
        );

        wishlistRepository.delete(wishlist);
    }

    @Override
    public ResponseWishDto getWishList(String userId, int nowPage, int pageSize) {
        log.debug("위시 리스트 조회 실행");

        ResponseUser user = feignTemplate.findUserByUserId(userId);

        PageRequest pageRequest = PageRequest.of(nowPage, pageSize);
        Page<Wishlist> pageList = wishlistRepository.findAllByUserId(userId, pageRequest);
        List<ProductInfo> productInfoList = pageList.stream().map(Wishlist::entityToVo).toList();

        return ResponseWishDto.builder()
                .userId(user.getUserId())
                .productInfoList(productInfoList)
                .build();
    }
}
