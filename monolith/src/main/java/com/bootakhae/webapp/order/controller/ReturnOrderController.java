package com.bootakhae.webapp.order.controller;

import com.bootakhae.webapp.order.dto.ReturnOrderDto;
import com.bootakhae.webapp.order.services.ReturnOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
