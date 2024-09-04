package com.bootakhae.orderservice.order.controller;

import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.services.OrderService;
import com.bootakhae.orderservice.order.services.OrderServiceImpl;
import com.bootakhae.orderservice.order.vo.request.RequestOrder;
import com.bootakhae.orderservice.order.vo.response.ResponseOrder;

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
    private final OrderServiceImpl orderServiceImpl;


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
     * 주문취소
     */
    @DeleteMapping("{orderId}")
    public ResponseEntity<ResponseOrder> cancelOrder(@PathVariable String orderId) {
        OrderDto orderDetails = orderService.cancelOrder(orderId);
        ResponseOrder response = orderDetails.dtoToVo();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    /**
     * 주문상세 조회
     */
    @GetMapping("{orderId}")
    public ResponseEntity<ResponseOrder> getOrderDetail(@PathVariable String orderId) {
        OrderDto orderDetails = orderService.getOrderDetails(orderId);
        ResponseOrder response = orderDetails.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 회원주문목록 조회
     */
    @GetMapping("users/{userId}")
    public ResponseEntity<List<ResponseOrder>> getUsersOrder(@PathVariable String userId,
                                                             @RequestParam(defaultValue = "0") int nowPage,
                                                             @RequestParam(defaultValue = "10") int pageSize
                                                             ) {
        List<OrderDto> orderDetailsList = orderService.getOrderListByUserId(userId, nowPage, pageSize);
        List<ResponseOrder> responseOrders = orderDetailsList.stream().map(OrderDto::dtoToVo).toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }

    @GetMapping("test1")
    public ResponseEntity<?> getOrders1(){
        List<OrderEntity> orderEntities = orderServiceImpl.getOrders1();
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }

    @GetMapping("test2")
    public ResponseEntity<?> getOrders2(){
        List<OrderEntity> orderEntities = orderServiceImpl.getOrders2();
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }

    @GetMapping("test3")
    public ResponseEntity<?> getOrders3(){
        List<OrderEntity> orderEntities = orderServiceImpl.getOrders3();
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }
}
