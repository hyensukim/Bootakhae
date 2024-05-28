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

import java.util.List;
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
        ProductDto productDetails = productService.getOneProduct(productId);
        ResponseProduct response = productDetails.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 재고 확인 및 감소
     * @from order-service
     */
    @PutMapping("decrease")
    public ResponseEntity<List<ResponseProduct>> checkAndDecreaseStock(@RequestBody List<ProductInfo> request){
        List<ProductDto> productDetailsList = productService.checkAndDecreaseStock(request.stream()
                .map(ProductInfo::voToDto)
                .collect(Collectors.toList()));
        List<ResponseProduct> response = productDetailsList.stream().map(ProductDto::dtoToVo).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 재고 복구
     * @from order-service
     */
    @PutMapping("restore")
    public ResponseEntity<Void> restoreStock(@RequestBody List<ProductInfo> request){
        productService.restoreStock(request.stream().map(ProductInfo::voToDto).collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
