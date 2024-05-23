package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.clients.vo.request.RequestProduct;
import com.bootakhae.orderservice.global.clients.vo.request.RequestStock;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import com.bootakhae.orderservice.order.vo.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    // 상품 조회
    @GetMapping("/api/v1/internal/products/{productId}")
    ResponseProduct getOneProduct(@PathVariable("productId") String productId);

    // 상품 목록 조회
    @PostMapping("/api/v1/internal/products")
    void checkStock(@RequestBody List<RequestProduct> productIds);

    @PutMapping("/api/v1/internal/products")
    List<ResponseProduct> updateStock(@RequestBody RequestStock requestStock);
}
