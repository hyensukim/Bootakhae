package com.bootakhae.webapp.product.services;

import com.bootakhae.webapp.product.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto productDetails);

    ProductDto getOneProduct(String productId);

    List<ProductDto> getAllProducts(int nowPage, int pageSize);
}
