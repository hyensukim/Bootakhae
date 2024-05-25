package com.bootakhae.payservice.vo.request;

import com.bootakhae.payservice.dto.PayDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPay {
    @NotBlank(message = "주문 번호를 입력 바랍니다.")
    private String orderId;
    @NotBlank(message = "결제 방식을 입력 바랍니다.")
    private String payMethod;
    @NotNull(message = "총 결제 금액을 입력 바랍니다.")
    private Long totalPrice;

    public PayDto voToDto() {
        return PayDto.builder()
                .orderId(this.orderId)
                .payMethod(this.payMethod)
                .totalPrice(this.totalPrice)
                .build();
    }
}
