package com.bootakhae.productservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductList {
    private int nowPage;
    private int totalPages;
    private Long totalProducts;
    private List<ResponseProduct> productList;
}
