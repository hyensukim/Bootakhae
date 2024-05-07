package com.bootakhae.payservice.controllers;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.services.PayService;
import com.bootakhae.payservice.vo.response.ResponsePay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pays")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("service is available");
    }

    /**
     * 결제 완료
     */
    @PutMapping("{payId}")
    public ResponseEntity<ResponsePay> completePayment(@PathVariable("payId") String payId){
        PayDto payDetails = payService.completePay(payId);
        return ResponseEntity.status(HttpStatus.OK).body(payDetails.dtoToVo());
    }
}
