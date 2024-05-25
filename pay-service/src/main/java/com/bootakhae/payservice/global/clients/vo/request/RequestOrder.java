package com.bootakhae.payservice.global.clients.vo.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestOrder {
    private String payId;
    private String orderId;
}
