package com.bootakhae.payservice.services;

import com.bootakhae.payservice.dto.PayDto;

public interface PayService {
    PayDto registerPay(PayDto payDetails);

    PayDto completePay(String payId);
}
