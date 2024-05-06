package com.bootakhae.payservice.vo.request;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.entities.PayEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPay {
    private String orderId;
    private String payMethod;

    public PayDto voToDto() {
        return PayDto.builder()
                .orderId(orderId)
                .payMethod(this.payMethod)
                .build();
    }
}
