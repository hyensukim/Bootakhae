package com.bootakhae.payservice.controllers;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.services.PayService;
import com.bootakhae.payservice.vo.response.ResponsePay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/internal/pays")
@RequiredArgsConstructor
public class PayFeignController {

    private final PayService payService;

    /**
     * 결제 상세 정보 조회
     * @from order-service
     */
    @GetMapping("{payId}")
    public ResponseEntity<ResponsePay> getOnePay(@PathVariable("payId") String payId) {
        PayDto payDetails = payService.getOnePay(payId);
        return ResponseEntity.status(HttpStatus.OK).body(payDetails.dtoToVo());
    }
}
