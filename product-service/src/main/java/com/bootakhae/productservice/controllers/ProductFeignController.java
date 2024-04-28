package com.bootakhae.productservice.controllers;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.services.ProductService;
import com.bootakhae.productservice.vo.response.ResponseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/internal/products")
@RequiredArgsConstructor
public class ProductFeignController {

    private final ProductService productService;

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
     * 위시 리스트 상품 목록 조회
     * - 한번에 여러 아이디 값의 상품들을 조회하기 위해 PostMapping
     */
    @PostMapping
    public ResponseEntity<List<ResponseProduct>> getProducts(@RequestBody List<String> productIds){
        List<ProductDto> productDetailsList = productService.getAllByProductIds(productIds);
        List<ResponseProduct> responseList = productDetailsList.stream().map(ProductDto::dtoToVo).toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    /**
     * 상품 재고 변경
     * @from order-service
     */
    @PutMapping("{productId}")
    public ResponseEntity<ResponseProduct> deductStock(@PathVariable("productId") String productId,@RequestBody Long stock){
        ProductDto dto = productService.updateStock(productId, stock);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
