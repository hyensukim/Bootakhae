package com.bootakhae.payservice.controllers;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.services.PayService;
import com.bootakhae.payservice.vo.request.RequestPay;
import com.bootakhae.payservice.vo.response.ResponsePay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/internal/pays")
@RequiredArgsConstructor
public class PayFeignController {

    private final PayService payService;

    @PostMapping
    public ResponseEntity<ResponsePay> payment(@RequestBody RequestPay request){
        PayDto payDetails =  payService.registerPay(request.voToDto());
        return ResponseEntity.status(HttpStatus.OK).body(payDetails.dtoToVo());
    }
}
