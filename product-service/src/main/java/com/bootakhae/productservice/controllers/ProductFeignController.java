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
        ProductDto dto = productService.getOneProduct(productId);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 위시 리스트 상품 목록 조회
     * - 한번에 여러 아이디 값의 상품들을 조회하기 위해 PostMapping
     * @from order-service
     */
    @PostMapping
    public ResponseEntity<List<ResponseProduct>> getProducts(@RequestBody List<String> productIds){
        List<ProductDto> productDetailsList = productService.getAllByProductIds(productIds);
        List<ResponseProduct> responseList = productDetailsList.stream().map(ProductDto::dtoToVo).toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    /**
     * 상품 재고 시스템
     * @from order-service
     */
    @PutMapping("{productId}")
    public ResponseEntity<ResponseProduct> updateProduct(@PathVariable("productId") String productId,
                                                         @RequestBody String stockProcess,
                                                         @RequestBody Long qty){
        ProductDto productDetails = productService.updateStock(stockProcess, productId, qty);
        return ResponseEntity.status(HttpStatus.OK).body(productDetails.dtoToVo());
    }

    /**
     * 상품 목록 재고 시스템
     * @from order-service
     */
    @PutMapping
    public ResponseEntity<List<ResponseProduct>> updateProduct(@RequestBody RequestStock request){
        List<ProductDto> productDetailsList = productService.updateStockList(request);
        return ResponseEntity.status(HttpStatus.OK).body(productDetailsList
                .stream()
                .map(ProductDto::dtoToVo)
                .collect(Collectors.toList())
        );
    }

    /**
     * 상품 재고 감소
     * @from order-service
     */
    @PutMapping("{productId}/decrease")
    public ResponseEntity<ResponseProduct> decreaseStock(@PathVariable("productId") String productId,@RequestBody Long qty){
        ProductDto dto = redissonInventoryFacade.decrease(productId, qty);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 재고 복구
     * @from order-service
     */
    @PutMapping("{productId}/restore")
    public ResponseEntity<ResponseProduct> restoreStock(@PathVariable("productId") String productId,@RequestBody Long qty){
        ProductDto dto = redissonInventoryFacade.restore(productId, qty);
        ResponseProduct response = dto.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
