package com.bootakhae.orderservice.global.exception;

import lombok.Getter;

@Getter
public class QtyLackException extends CustomException{

    public QtyLackException(ErrorCode errorCode) {
        super(errorCode, "재고가 부족합니다.");
    }
}
