package com.bootakhae.productservice.vo.request;

import com.bootakhae.productservice.dto.ProductInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Feign 으로 요청되는 상품 정보
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    private String productId;
    private Long qty;

    public ProductInfoDto voToDto(){
        return ProductInfoDto.builder()
                .productId(this.productId)
                .qty(this.qty)
                .build();
    }
}
