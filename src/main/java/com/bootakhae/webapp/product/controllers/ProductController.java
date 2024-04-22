package com.bootakhae.webapp.product.controllers;


import com.bootakhae.webapp.product.dto.ProductDto;
import com.bootakhae.webapp.product.services.ProductService;
import com.bootakhae.webapp.product.vo.request.RequestProduct;
import com.bootakhae.webapp.product.vo.response.ResponseProduct;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("service is available");
    }

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ResponseProduct> registerProduct(@Valid @RequestBody RequestProduct request){
        ProductDto dto = request.voToDto();
        dto = productService.registerProduct(dto);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("{productId}")
    public ResponseEntity<ResponseProduct> getOneProduct(@PathVariable("productId") String productId){
        ProductDto dto = productService.getOneProduct(productId);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<ResponseProduct>> getAllProducts(@RequestParam(defaultValue = "0") int nowPage,
                                                                @RequestParam(defaultValue = "5") int pageSize){
        List<ProductDto> dtoList = productService.getAllProducts(nowPage, pageSize);
        List<ResponseProduct> response = dtoList.stream().map(ProductDto::dtoToVo).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
