package com.bootakhae.payservice.services;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.entities.PayEntity;
import com.bootakhae.payservice.global.clients.OrderClient;
import com.bootakhae.payservice.global.clients.vo.request.RequestOrder;
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

        if(payRepository.existsPayEntityByOrderId(payDetails.getOrderId())){
            throw new CustomException(ErrorCode.ALREADY_COMPLETED_PAY);
        }

        PayEntity pay = payDetails.dtoToEntity();

        // 주문 서비스에 결제 완료 알리기
        orderClient.updateOrder(RequestOrder.builder()
                .orderId(pay.getOrderId())
                .payId(pay.getPayId())
                .build()
        );

        pay = payRepository.save(pay);
        return pay.entityToDto();
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
