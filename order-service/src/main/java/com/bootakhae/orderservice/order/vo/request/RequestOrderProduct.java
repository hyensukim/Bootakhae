package com.bootakhae.orderservice.order.vo.request;

import com.bootakhae.orderservice.order.dto.OrderProductDto;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestOrderProduct {
    private String productId;
    private Long qty;

    public OrderProductDto voToDto(){
        return OrderProductDto.builder()
                .productId(this.productId)
                .qty(this.qty)
                .build();
    }
}
