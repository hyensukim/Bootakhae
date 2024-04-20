package com.bootakhae.webapp.wishlist.services;

import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.product.repositories.ProductRepository;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.repositories.UserRepository;
import com.bootakhae.webapp.wishlist.dto.request.RequestWishDto;
import com.bootakhae.webapp.wishlist.dto.response.ResponseWishDto;
import com.bootakhae.webapp.wishlist.entities.WishListEntity;
import com.bootakhae.webapp.wishlist.repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{

    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public ResponseWishDto includeWish(RequestWishDto requestWishDto) {
        UserEntity user = userRepository.findByUserId(requestWishDto.getUserId()).orElseThrow(
                () -> new RuntimeException("찜 등록 : 로그인 후 이용 바랍니다.")
        );

        ProductEntity product = productRepository.findByProductId(requestWishDto.getProductId()).orElseThrow(
                () -> new RuntimeException("찜 등록 : 등록되지 않은 상품입니다.")
        );

        WishListEntity wishList = WishListEntity.builder()
                .user(user)
                .product(product)
                .quantity(requestWishDto.getQuantity())
                .build();

        wishList = wishListRepository.save(wishList);

        return wishList.entityToDto();
    }

    @Transactional
    @Override
    public ResponseWishDto updateQty(RequestWishDto requestWishDto) {
        UserEntity user = userRepository.findByUserId(requestWishDto.getUserId()).orElseThrow(
                () -> new RuntimeException("찜 수량 변경 : 로그인 후 이용 바랍니다.")
        );

        ProductEntity product = productRepository.findByProductId(requestWishDto.getProductId()).orElseThrow(
                () -> new RuntimeException("찜 수량 변경 : 등록되지 않은 상품입니다.")
        );

        WishListEntity wishList = wishListRepository.findByProductAndUser(product, user).orElseThrow(
                () -> new RuntimeException("찜 수량 변경 : 존재하지 않는 찜목록 입니다.")
        );

        wishList.changeQty(requestWishDto.getQuantity());
        return wishList.entityToDto();
    }

    @Transactional
    @Override
    public void excludeWish(String userId, String productId) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("찜 등록 : 로그인 후 이용 바랍니다.")
        );

        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new RuntimeException("찜 등록 : 등록되지 않은 상품입니다.")
        );

        WishListEntity wishList = wishListRepository.findByProductAndUser(product, user).orElseThrow(
                () -> new RuntimeException("찜 수량 변경 : 존재하지 않는 찜목록 입니다.")
        );

        wishListRepository.delete(wishList);
    }
}
