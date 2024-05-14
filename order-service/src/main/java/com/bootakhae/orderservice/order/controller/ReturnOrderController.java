package com.bootakhae.orderservice.order.controller;

import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.bootakhae.orderservice.order.services.OrderService;
import com.bootakhae.orderservice.order.services.ReturnOrderService;
import com.bootakhae.orderservice.order.vo.request.RequestReturnOrder;
import com.bootakhae.orderservice.order.vo.response.ResponseOrder;
import com.bootakhae.orderservice.order.vo.response.ResponseReturn;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/return/orders")
@RequiredArgsConstructor
public class ReturnOrderController {

    private final OrderService orderService;

    private final ReturnOrderService returnOrderService;

    /**
     * 반품하기
     */
    @PostMapping
    public ResponseEntity<ResponseOrder> returnDoneOrder(@RequestBody RequestReturnOrder request){
        OrderDto orderDetails = orderService.returnOrderedProduct(request.voToDto());
        ResponseOrder response = orderDetails.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 반품 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ResponseReturn>> getReturnOrders(
            @RequestParam(defaultValue = "0") int nowPage,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        List<ReturnOrderDto> returnOrderList = returnOrderService.getReturnOrders(nowPage, pageSize);
        List<ResponseReturn> responseReturns = returnOrderList.stream().map(ReturnOrderDto::dtoToVo).toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseReturns);
    }
}
