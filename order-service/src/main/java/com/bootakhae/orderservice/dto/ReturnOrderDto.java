package com.bootakhae.orderservice.dto;

import com.bootakhae.orderservice.global.constant.Reason;
import com.bootakhae.orderservice.entities.OrderEntity;
import com.bootakhae.orderservice.entities.ReturnOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderDto {
    private Reason reason;

    public ReturnOrderEntity dtoToEntity(OrderEntity order) {
        return ReturnOrderEntity.builder()
                .order(order)
                .reason(this.reason)
                .build();
    }
}
