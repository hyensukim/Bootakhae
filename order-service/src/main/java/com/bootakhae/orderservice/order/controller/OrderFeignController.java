package com.bootakhae.orderservice.order.controller;

import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.services.OrderService;
import com.bootakhae.orderservice.order.vo.response.ResponseOrder;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/internal/orders")
@RequiredArgsConstructor
public class OrderFeignController {

    private final OrderService orderService;

    /**
     * 결제 완료
     * @from pay-service
     */
    @PutMapping
    public ResponseEntity<ResponseOrder> updateOrder(@RequestBody String payId) {
        OrderDto orderDetails = orderService.completePayment(payId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDetails.dtoToVo());
    }

    // todo : 삭제 예정
    @GetMapping
    public ResponseEntity<List<ResponseOrder>> getAll(){
        List<OrderDto> orderDetailsList = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsList
                .stream()
                .map(OrderDto::dtoToVo)
                .collect(Collectors.toList())
        );
    }
}
