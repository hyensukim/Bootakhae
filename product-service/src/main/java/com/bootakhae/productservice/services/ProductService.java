package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductListDto;
import com.bootakhae.productservice.vo.request.RequestStock;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto productDetails);

    ProductListDto registerEventProduct(List<ProductDto> productDetailsList);

    List<ProductDto> updateStock(RequestStock request);

    ProductDto decreaseStock(String productId, Long qty);

    ProductDto decreaseStockPessimistic(String productId, Long qty); // test

    void decreaseStockOptimistic(String productId, Long qty); // test

    ProductDto getOneProduct(String productId);

    ProductListDto getAllProducts(int nowPage, int pageSize);

    List<ProductDto> getAllByProductIds(List<String> productIds);

    void openEventProduct();
}
