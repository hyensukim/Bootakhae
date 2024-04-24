package com.bootakhae.orderservice.controller;

import com.bootakhae.orderservice.services.ReturnOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders/refund")
@RequiredArgsConstructor
public class ReturnOrderController {
    private final ReturnOrderService returnOrderService;

//    @GetMapping
//    public ResponseEntity<List<ResponseReturn>> getReturnAll(@RequestParam(defaultValue = "0") int nowPage,
//                                                             @RequestParam(defaultValue = "5") int pageSize){
//        List<ReturnOrderDto> details = returnOrderService.getReturnOrders(nowPage,pageSize);
//
//    }

}
