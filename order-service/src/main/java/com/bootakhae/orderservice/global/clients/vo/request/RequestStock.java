package com.bootakhae.orderservice.global.clients.vo.request;

import com.bootakhae.orderservice.order.dto.OrderProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestStock {
    private String stockProcess;
    private List<OrderProductDto> productInfoList;
}
