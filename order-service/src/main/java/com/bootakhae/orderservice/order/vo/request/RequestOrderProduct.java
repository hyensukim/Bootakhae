package com.bootakhae.orderservice.order.vo.request;

import com.bootakhae.orderservice.global.exception.CustomException;
import com.bootakhae.orderservice.global.exception.ErrorCode;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestOrderProduct {
    private String productId;
    private Long qty;

    public OrderProductDto voToDto(){
        if(this.productId == null || this.qty <= 0) throw new CustomException(ErrorCode.ANYONE_ELSE_PRODUCT);
        return OrderProductDto.builder()
                .productId(this.productId)
                .qty(this.qty)
                .build();
    }
}
