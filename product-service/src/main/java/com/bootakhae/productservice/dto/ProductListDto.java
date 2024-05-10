package com.bootakhae.productservice.dto;

import com.bootakhae.productservice.vo.response.ResponseProduct;
import com.bootakhae.productservice.vo.response.ResponseProductList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto implements Serializable {
    private int totalPages;

    private Long totalProducts;

    private List<ProductDto> productList;

    public ResponseProductList dtoToVo(){
        return ResponseProductList.builder()
                .totalPages(totalPages)
                .totalProducts(totalProducts)
                .productList(productList.stream().map(ProductDto::dtoToVo).toList())
                .build();
    }
}
