package com.bootakhae.webapp.order.vo.request;

import com.bootakhae.webapp.order.constant.Reason;
import com.bootakhae.webapp.order.dto.ReturnOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestReturnOrder {
    private String reason; // Front 에서 각 name 으로 이유를 전달하면, valueOf를 사용하여 적용

    public ReturnOrderDto voToDto(){
        return ReturnOrderDto.builder()
                .reason(Reason.valueOf(this.reason))
                .build();
    }
}
