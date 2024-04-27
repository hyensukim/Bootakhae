package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.wishlist.vo.response.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/internal/products/{productId}")
    ResponseProduct getOneProduct(@PathVariable("productId") String productId);

    @GetMapping("/api/v1/internal/products")
    List<ResponseProduct> getProducts(@RequestParam("productList") List<String> productList);

    @PutMapping("/api/v1/internal/products/{productId}")
    ResponseProduct updateStock(@PathVariable("productId") String productId, @RequestBody Long stock);
}
