package com.bootakhae.orderservice.order.dto;

import com.bootakhae.orderservice.global.constant.Reason;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.entities.ReturnOrderEntity;
import com.bootakhae.orderservice.order.vo.response.ResponseReturn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderDto {
    private String orderId;
    private Reason reason;

    public ReturnOrderEntity dtoToEntity(OrderEntity order) {
        return ReturnOrderEntity.builder()
                .order(order)
                .reason(this.reason)
                .build();
    }

    public ResponseReturn dtoToVo(){
        return ResponseReturn.builder()
                .orderId(orderId)
                .reason(reason.name())
                .build();
    }
}
