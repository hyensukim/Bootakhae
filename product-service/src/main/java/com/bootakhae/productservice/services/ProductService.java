package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductInfoDto;
import com.bootakhae.productservice.dto.ProductListDto;
import com.bootakhae.productservice.vo.request.RequestStock;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto productDetails);

    ProductListDto registerEventProduct(List<ProductDto> productDetailsList);

    List<ProductDto> updateStock(RequestStock request);

    List<ProductDto> checkAndDecreaseStock(List<ProductInfoDto> productInfoList);

    void restoreStock(List<ProductInfoDto> productInfoList);

    ProductDto decreaseStockTest(String productId, Long qty);

    ProductDto decreaseStockPessimistic(String productId, Long qty); // test

    void decreaseStockOptimistic(String productId, Long qty); // test

    ProductDto getOneProduct(String productId);

    ProductListDto getAllProducts(int nowPage, int pageSize);

    void checkStock(List<ProductInfoDto> productInfoList);

    void openEventProduct();
}
