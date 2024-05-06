package com.bootakhae.payservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePay {
    private String payId;

    private String payMethod;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
