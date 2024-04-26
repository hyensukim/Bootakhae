package com.bootakhae.orderservice.clients;

import com.bootakhae.orderservice.wishlist.vo.response.ResponseProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/internal/products/{productId}")
    ResponseProduct getOneProduct(@PathVariable("productId") String productId);

    @PutMapping("api/v1/internal/products/{productId}")
    ResponseProduct updateStock(@PathVariable("productId") String productId, @RequestBody Long stock);
}
