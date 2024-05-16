package com.bootakhae.orderservice.global.exception;

public class ClientException extends CustomException{
    public ClientException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
