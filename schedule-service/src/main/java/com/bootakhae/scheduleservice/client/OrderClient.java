package com.bootakhae.scheduleservice.client;

import com.bootakhae.scheduleservice.client.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("order-service")
public interface OrderClient {

    // todo : 삭제예정
    @GetMapping("api/v1/internal/orders")
    List<ResponseOrder> getAllOrders();
}
