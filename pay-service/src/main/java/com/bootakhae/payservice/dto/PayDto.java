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

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PayEntity dtoToEntity() {
        return PayEntity.builder()
                .orderId(this.orderId)
                .payMethod(this.payMethod)
                .build();
    }

    public ResponsePay dtoToVo(){
        return ResponsePay.builder()
                .payId(this.payId)
                .payMethod(this.payMethod)
                .status(this.status)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
