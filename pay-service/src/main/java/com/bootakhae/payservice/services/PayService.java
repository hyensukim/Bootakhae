package com.bootakhae.payservice.services;

import com.bootakhae.payservice.dto.PayDto;

public interface PayService {
    PayDto registerPay(PayDto payDetails);

    PayDto getOnePay(String payId);
}
