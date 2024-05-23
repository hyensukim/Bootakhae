package com.bootakhae.productservice.controllers;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.facade.RedissonInventoryFacade;
import com.bootakhae.productservice.services.ProductService;
import com.bootakhae.productservice.vo.request.ProductInfo;
import com.bootakhae.productservice.vo.request.RequestStock;
import com.bootakhae.productservice.vo.response.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/internal/products")
@RequiredArgsConstructor
public class ProductFeignController {

    private final ProductService productService;
    private final RedissonInventoryFacade redissonInventoryFacade;

    /**
     * 상품 조회
     * @from order-service
     */
    @GetMapping("{productId}")
    public ResponseEntity<ResponseProduct> getOneProduct(@PathVariable("productId") String productId){
        ProductDto dto = productService.getOneProduct(productId);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 재고 확인
     * @from order-service
     */
    @PostMapping
    public ResponseEntity<Void> checkStock(@RequestBody List<ProductInfo> productInfoList){
        productService.checkStock(productInfoList.stream()
                .map(ProductInfo::voToDto)
                .collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 상품 목록 재고 시스템
     * @from order-service
     */
    @PutMapping
    public ResponseEntity<List<ResponseProduct>> updateProduct(@RequestBody RequestStock request){
        List<ProductDto> productDetailsList = productService.updateStock(request);
        return ResponseEntity.status(HttpStatus.OK).body(productDetailsList
                .stream()
                .map(ProductDto::dtoToVo)
                .collect(Collectors.toList())
        );
    }
}
