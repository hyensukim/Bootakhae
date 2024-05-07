package com.bootakhae.productservice.controllers;


import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductListDto;
import com.bootakhae.productservice.services.ProductService;
import com.bootakhae.productservice.vo.request.RequestEventProduct;
import com.bootakhae.productservice.vo.request.RequestProduct;
import com.bootakhae.productservice.vo.response.ResponseProduct;
import com.bootakhae.productservice.vo.response.ResponseProductList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
     * 특정 상품 이벤트 항목으로 등록 - 상품 수정
     */
    @PutMapping
    public ResponseEntity<ResponseProductList> registerEventProduct(@RequestBody List<RequestEventProduct> request){
        ProductListDto productList = productService.registerEventProduct(request
                .stream()
                .map(RequestEventProduct::voToDto).collect(Collectors.toList()));
        ResponseProductList response = productList.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    public ResponseEntity<ResponseProductList> getAllProducts(@RequestParam(defaultValue = "0") int nowPage,
                                                                @RequestParam(defaultValue = "5") int pageSize){
        ProductListDto productList = productService.getAllProducts(nowPage, pageSize);
        ResponseProductList response = productList.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
