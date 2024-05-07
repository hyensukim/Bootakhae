package com.bootakhae.payservice.services;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.global.clients.vo.response.ResponseOrder;

public interface PayService {
    PayDto registerPay(PayDto payDetails);

    PayDto completePay(String payId);

    PayDto getOnePay(String payId);
}
