package com.bootakhae.payservice.global.clients;

import com.bootakhae.payservice.global.clients.vo.request.RequestOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("order-service")
public interface OrderClient {

    /**
     * 주문 상태 변경 - 결제 완료 및 결제 id 등록
     */
    @PutMapping("api/v1/internal/orders")
    void updateOrder(@RequestBody RequestOrder request);

}
