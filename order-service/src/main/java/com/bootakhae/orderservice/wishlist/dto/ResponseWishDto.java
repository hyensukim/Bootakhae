package com.bootakhae.orderservice.wishlist.dto;

import com.bootakhae.orderservice.order.vo.ProductInfo;
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
public class ResponseWishDto implements Serializable {

    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductInfo productInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductInfo> productInfoList;

}