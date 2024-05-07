package com.bootakhae.orderservice.order.controller;

import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.services.OrderService;
import com.bootakhae.orderservice.order.vo.response.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/internal/orders")
@RequiredArgsConstructor
public class OrderFeignController {

    private final OrderService orderService;

    @PutMapping
    public ResponseEntity<ResponseOrder> updateOrder(@RequestBody String payId) {
        OrderDto orderDetails = orderService.completePayment(payId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDetails.dtoToVo());
    }
}
