package com.bootakhae.orderservice.wishlist.services;

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

    private final UserClient userClient;
    private final ProductClient productClient;

    private final WishlistRepository wishlistRepository;

    @Transactional
    @Override
    public ResponseWishDto includeWish(RequestWishDto requestWishDto) {
        log.debug("위시 리스트 등록 실행");

        ResponseUser user;
        ResponseProduct product;
        try{
            user = userClient.getUser(requestWishDto.getUserId());
            product = productClient.getOneProduct(requestWishDto.getProductId());
        }catch(FeignException.FeignClientException e){
            throw new CustomException(ErrorCode.FEIGN_CLIENT_ERROR);
        }

        Wishlist wishlist;
        wishlistRepository.findByUserIdAndProductId(user.getResUserId(), product.getProductId()).ifPresent(
                wlist -> {throw new CustomException(ErrorCode.DUPLICATE_WISHLIST);}
        );

        wishlist = wishlistRepository
                .save(new Wishlist(user.getResUserId(), product.getProductId(), requestWishDto.getQty()));

        return wishlist.entityToDto();
    }

    @Transactional
    @Override
    public ResponseWishDto updateQty(RequestWishDto requestWishDto) {
        log.debug("위시 리스트 수량 변경 실행");

        ResponseUser user;
        ResponseProduct product;
        try{
            user = userClient.getUser(requestWishDto.getUserId());
            product = productClient.getOneProduct(requestWishDto.getProductId());
        }catch(FeignException.FeignClientException e){
            throw new CustomException(ErrorCode.FEIGN_CLIENT_ERROR);
        }

        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(user.getResUserId(), product.getProductId())
                .map(ws -> {
                    ws.changeQty(requestWishDto.getQty());
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

        ResponseUser user;
        try{
            user = userClient.getUser(userId);
        }catch(FeignException.FeignClientException e){
            throw new CustomException(ErrorCode.FEIGN_CLIENT_ERROR);
        }

        PageRequest pageRequest = PageRequest.of(nowPage, pageSize);
        Page<Wishlist> pageList = wishlistRepository.findAllByUserId(userId, pageRequest);
        List<ProductInfo> productInfoList = pageList.stream().map(Wishlist::entityToVo).toList();

        return ResponseWishDto.builder()
                .userId(user.getResUserId())
                .productInfoList(productInfoList)
                .build();
    }
}
