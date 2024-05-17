package com.bootakhae.payservice.services;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.entities.PayEntity;
import com.bootakhae.payservice.global.clients.OrderClient;
import com.bootakhae.payservice.global.clients.vo.response.ResponseOrder;
import com.bootakhae.payservice.global.exception.CustomException;
import com.bootakhae.payservice.global.exception.ErrorCode;
import com.bootakhae.payservice.repositories.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;
    private final OrderClient orderClient;

    @Transactional
    @Override
    public PayDto registerPay(PayDto payDetails) {
        log.debug("결제 생성 실행");
        PayEntity pay = payDetails.dtoToEntity();
        pay = payRepository.save(pay);
        return pay.entityToDto();
    }

    @Transactional
    @Override
    public PayDto completePay(String payId) {
        log.debug("결제 완료 변경 실행");

        PayEntity pay = payRepository.findByPayId(payId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_PAY)
        );

        pay.completePayment();
        
        // 주문 서비스에 결제 완료 알리기
        ResponseOrder order = orderClient.updateOrder(payId);

        return pay.entityToDto(order.getOrderId(), order.getOrderStatus());
    }

    @Override
    public PayDto getOnePay(String payId) {
        log.debug("결제 조회");
        PayEntity pay = payRepository.findByPayId(payId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXISTS_PAY)
        );

        return pay.entityToDto();
    }
}
