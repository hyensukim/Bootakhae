package com.bootakhae.orderservice.global.clients.vo.request;

import com.bootakhae.orderservice.order.dto.PayDto;
import com.bootakhae.orderservice.order.vo.ProductInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPay {
    private String orderId;
    private String payId;
    private String payMethod;
    private Long totalPrice;
    @JsonIgnore
    private List<ProductInfo> productList;

    public PayDto voToDto(){
        return PayDto.builder()
                .orderId(this.orderId)
                .payId(this.payId)
                .build();
    }
}
