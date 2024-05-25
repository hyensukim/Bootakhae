package com.bootakhae.orderservice.order.controller;

import com.bootakhae.orderservice.global.clients.vo.request.RequestPay;
import com.bootakhae.orderservice.order.services.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Void> completePayment(@RequestBody RequestPay request) {
        orderService.completePayment(request.voToDto());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
