package com.bootakhae.webapp.wishlist.dto.response;

import com.bootakhae.webapp.wishlist.vo.ProductInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWishDto {

    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductInfo productInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductInfo> productInfoList;

}