package com.bootakhae.payservice.global.clients;

import com.bootakhae.payservice.global.clients.vo.response.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("order-service")
public interface OrderClient {

    @PutMapping("api/v1/internal/orders")
    ResponseOrder updateOrder(@RequestBody String payId);

}
