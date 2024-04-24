package com.bootakhae.orderservice.controller;

import com.bootakhae.orderservice.dto.OrderDto;
import com.bootakhae.orderservice.services.OrderService;
import com.bootakhae.orderservice.vo.request.RequestOrder;
import com.bootakhae.orderservice.vo.request.RequestReturnOrder;
import com.bootakhae.orderservice.vo.request.RequestWishlist;
import com.bootakhae.orderservice.vo.response.ResponseOrder;
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

    /**
     * 주문 취소
     */
    @DeleteMapping("{orderId}")
    public ResponseEntity<ResponseOrder> removeOrder(@PathVariable String orderId) {
        OrderDto orderDetails = orderService.removeOrder(orderId);
        ResponseOrder response = orderDetails.dtoToVo();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("{orderId}")
    public ResponseEntity<ResponseOrder> getOrderDetail(@PathVariable String orderId) {
        OrderDto orderDetails = orderService.getOrderDetails(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDetails.dtoToVo());
    }

    /**
     * 회원 주문 목록 조회
     */
    @GetMapping("user/{userId}")
    public ResponseEntity<List<ResponseOrder>> getUsersOrder(@PathVariable String userId,
                                                             @RequestParam(defaultValue = "0") int nowPage,
                                                             @RequestParam(defaultValue = "5") int pageSize
                                                             ) {
        List<OrderDto> orderDetailsList = orderService.getOrderListByUserId(userId, nowPage, pageSize);
        List<ResponseOrder> responseOrders = orderDetailsList.stream().map(OrderDto::dtoToVo).toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }

    /**
     * 반품하기
     */
    @PostMapping("return/{orderId}")
    public ResponseEntity<ResponseOrder> returnDoneOrder(@PathVariable("orderId") String orderId,
                                                         @RequestBody RequestReturnOrder request){
        OrderDto orderDetails = orderService.returnOrderedProduct(orderId, request.voToDto());
        return ResponseEntity.status(HttpStatus.OK).body(orderDetails.dtoToVo());
    }

    /**
     * 주문 수정
     */
//    @PutMapping("{orderId}")
//    public ResponseEntity<ResponseOrder> updateOrder(@PathVariable String orderId, @RequestBody RequestOrder request) {
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
}
