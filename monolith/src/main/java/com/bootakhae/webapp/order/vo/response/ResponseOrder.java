package com.bootakhae.webapp.order.vo.response;

import com.bootakhae.webapp.order.constant.Status;
import com.bootakhae.webapp.order.dto.OrderProductDto;
import com.bootakhae.webapp.order.dto.ReturnOrderDto;
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
    private Long totalPrice;
    private String address1;
    private String address2;
    private String phone;
    private LocalDateTime createdAt;
    private Status orderStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReturnOrderDto returnOrder;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderProductDto orderedProduct;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderProductDto> orderedProducts;
}
