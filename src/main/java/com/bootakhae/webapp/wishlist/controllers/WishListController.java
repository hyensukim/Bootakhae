package com.bootakhae.webapp.wishlist.controllers;

import com.bootakhae.webapp.wishlist.dto.request.RequestWishDto;
import com.bootakhae.webapp.wishlist.dto.response.ResponseWishDto;
import com.bootakhae.webapp.wishlist.services.WishListService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wishes")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("service is available");
    }

    @PostMapping
    public ResponseEntity<ResponseWishDto> addWish(@RequestBody RequestWishDto request){
        ResponseWishDto response = wishListService.includeWish(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping
    public ResponseEntity<ResponseWishDto> updateQty(@RequestBody RequestWishDto request){
        ResponseWishDto response = wishListService.updateQty(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{userId}/{productId}")
    public ResponseEntity<Void> removeWish(@PathVariable String userId, @PathVariable String productId){
        wishListService.excludeWish(userId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
