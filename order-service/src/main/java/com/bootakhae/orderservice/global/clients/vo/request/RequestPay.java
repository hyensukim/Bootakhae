package com.bootakhae.orderservice.global.clients.vo.request;

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
    private String payMethod;
    private Long totalPrice;
    @JsonIgnore
    private List<ProductInfo> productList;
}
