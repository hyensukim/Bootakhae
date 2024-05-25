package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.clients.vo.request.RequestPay;
import com.bootakhae.orderservice.global.clients.vo.response.ResponsePay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pay-service")
public interface PayClient {

    @GetMapping("api/v1/internal/pays/{payId}")
    ResponsePay getOnePay(@PathVariable String payId);
}
