package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto productDetails);

    ProductDto getOneProduct(String productId);

    List<ProductDto> getAllProducts(int nowPage, int pageSize);
}
