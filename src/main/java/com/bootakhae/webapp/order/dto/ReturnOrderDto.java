package com.bootakhae.webapp.order.dto;

import com.bootakhae.webapp.order.constant.Reason;
import com.bootakhae.webapp.order.entities.OrderEntity;
import com.bootakhae.webapp.order.entities.ReturnOrderEntity;
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
