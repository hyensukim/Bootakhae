package com.bootakhae.payservice.services;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.entities.PayEntity;
import com.bootakhae.payservice.repositories.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;

    @Override
    public PayDto registerPay(PayDto payDetails) {
        log.debug("결제 생성 실행");
        PayEntity pay = payDetails.dtoToEntity();
        pay = payRepository.save(pay);
        return pay.entityToDto();
    }

    @Override
    public PayDto completePay(String payId) {
        log.debug("결제 완료 변경 실행");
        PayEntity pay = payRepository.findByPayId(payId).orElseThrow(
                () -> new RuntimeException("생성되지 않은 결제 내역입니다.")
        );
        pay.completePayment();
        
        // 주문 서비스에 결제 완료 알리기
        return null;
    }
}
