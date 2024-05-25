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

    // 재고 변경
    @PutMapping("/api/v1/internal/products")
    List<ResponseProduct> updateStock(@RequestBody RequestStock requestStock);

    // 재고 확인 + 감소
    @PutMapping("/api/v1/internal/products/decrease")
    List<ResponseProduct> checkAndDecreaseStock(@RequestBody List<RequestProduct> productInfoList);

    // 재고 복구
    @PutMapping("/api/v1/internal/products/restore")
    void restoreStock(@RequestBody List<RequestProduct> productInfoList);
}
