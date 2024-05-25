package com.bootakhae.payservice.dto;

import com.bootakhae.payservice.entities.PayEntity;
import com.bootakhae.payservice.vo.response.ResponsePay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayDto {
    private String payId;

    private String orderId;

    private String payMethod;

    private Long totalPrice;

    private LocalDateTime createdAt;

    public PayEntity dtoToEntity() {
        return PayEntity.builder()
                .orderId(this.orderId)
                .payMethod(this.payMethod)
                .totalPrice(this.totalPrice)
                .build();
    }

    public ResponsePay dtoToVo(){
        return ResponsePay.builder()
                .payId(this.payId)
                .orderId(this.orderId)
                .payMethod(this.payMethod)
                .totalPrice(this.totalPrice)
                .createdAt(this.createdAt)
                .build();
    }
}
