package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.order.dto.ReturnOrderDto;

import java.util.List;

public interface ReturnOrderService {
    
    // 반품한 상품 목록 조회
    List<ReturnOrderDto> getReturnOrders(int nowPage, int pageSize);
}
