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

    private String orderStatus;

    private String payMethod;

    private Long totalPrice;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
                .orderStatus(this.orderStatus)
                .payMethod(this.payMethod)
                .status(this.status)
                .totalPrice(this.totalPrice)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
