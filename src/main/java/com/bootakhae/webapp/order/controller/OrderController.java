package com.bootakhae.webapp.order.controller;

import com.bootakhae.webapp.order.dto.OrderDto;
import com.bootakhae.webapp.order.services.OrderService;
import com.bootakhae.webapp.order.vo.request.RequestOrder;
import com.bootakhae.webapp.order.vo.request.RequestWishlist;
import com.bootakhae.webapp.order.vo.response.ResponseOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("service is available");
    }

    /**
     * 주문하기
     */
    @PostMapping
    public ResponseEntity<ResponseOrder> createOrder(@Valid @RequestBody RequestOrder request) {
        OrderDto orderDetails =  orderService.registerOrder(request.voToDto());
        ResponseOrder response = orderDetails.dtoToVo();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 위시리스트 주문하기
     */
    @PostMapping("wishlist")
    public ResponseEntity<ResponseOrder> createOrderByWishlist(@Valid @RequestBody RequestWishlist request) {
        ResponseOrder response = orderService.registerOrders(request.voToDto()).dtoToVo();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<ResponseOrder> removeOrder(@PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{orderId}")
    public ResponseEntity<ResponseOrder> getOrderDetail(@PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("{orderId}")
    public ResponseEntity<ResponseOrder> updateOrder(@PathVariable String orderId, @RequestBody RequestOrder request) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<ResponseOrder> getUsersOrder(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
