package com.bootakhae.orderservice.order.dto;

import com.bootakhae.orderservice.global.clients.vo.request.RequestProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private String productId;
    private Long qty; // 주문수량

    public RequestProduct dtoToVo(){
        return RequestProduct.builder()
                .productId(this.productId)
                .qty(this.qty)
                .build();
    }
}
