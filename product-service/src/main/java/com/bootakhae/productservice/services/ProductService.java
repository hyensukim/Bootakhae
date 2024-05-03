package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductListDto;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto productDetails);

    ProductListDto registerEventProduct(List<ProductDto> productDetailsList);

    ProductDto updateStock(String productId, Long stock);

    ProductDto decreaseStock(String productId, Long qty);

    ProductDto decreaseStockPessimistic(String productId, Long qty);

    ProductDto decreaseStockOptimistic(String productId, Long qty);

    ProductDto restoreStock(String productId, Long qty);

    ProductDto getOneProduct(String productId);

    ProductListDto getAllProducts(int nowPage, int pageSize);

    List<ProductDto> getAllByProductIds(List<String> productIds);
}
