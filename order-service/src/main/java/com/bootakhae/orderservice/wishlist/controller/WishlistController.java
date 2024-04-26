package com.bootakhae.orderservice.wishlist.controller;


import com.bootakhae.orderservice.wishlist.dto.RequestWishDto;
import com.bootakhae.orderservice.wishlist.dto.ResponseWishDto;
import com.bootakhae.orderservice.wishlist.services.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wishes")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishListService;

    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("service is available");
    }

    /**
     * 위시 리스트 등록
     */
    @PostMapping
    public ResponseEntity<ResponseWishDto> addWish(@Valid @RequestBody RequestWishDto request){
        ResponseWishDto response = wishListService.includeWish(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 위시 리스트 수량 변경
     */
    @PutMapping
    public ResponseEntity<ResponseWishDto> updateQty(@Valid @RequestBody RequestWishDto request){
        ResponseWishDto response = wishListService.updateQty(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 위시 리스트 삭제
     */
    @DeleteMapping("{userId}/{productId}")
    public ResponseEntity<Void> removeWish(@PathVariable String userId, @PathVariable String productId){
        wishListService.excludeWish(userId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 위시 리스트 조회
     */
    @GetMapping("{userId}")
    public ResponseEntity<ResponseWishDto> wishList(@PathVariable String userId,
                                                    @RequestParam(defaultValue = "0") int nowPage,
                                                    @RequestParam(defaultValue = "6") int pageSize){
        ResponseWishDto response = wishListService.getWishList(userId, nowPage, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
