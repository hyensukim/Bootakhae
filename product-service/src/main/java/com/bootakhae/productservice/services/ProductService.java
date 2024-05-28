package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductInfoDto;
import com.bootakhae.productservice.dto.ProductListDto;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto productDetails);

    void deleteProduct(String productId);

    ProductListDto registerEventProduct(List<ProductDto> productDetailsList);

    void openEventProduct();

    List<ProductDto> checkAndDecreaseStock(List<ProductInfoDto> productInfoList);

    void restoreStock(List<ProductInfoDto> productInfoList);

    ProductDto getOneProduct(String productId);

    ProductListDto getNormalProducts(int nowPage, int pageSize);

    ProductListDto getEventProducts(int nowPage, int pageSize);

    ProductDto decreaseStockTest(String productId, Long qty);

    ProductDto decreaseStockPessimistic(String productId, Long qty); // test

    void decreaseStockOptimistic(String productId, Long qty); // test

}
