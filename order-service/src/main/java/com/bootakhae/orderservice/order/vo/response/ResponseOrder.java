package com.bootakhae.orderservice.order.vo.response;

import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOrder {
    private String orderId;
    private String userId;
    private String payId;
//    private String payMethod;
    private Long totalPrice;
    private String address1;
    private String address2;
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime returnAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status orderStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderProductDto> orderedProducts;
}
