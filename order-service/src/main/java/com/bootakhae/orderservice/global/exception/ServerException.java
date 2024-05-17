package com.bootakhae.orderservice.global.exception;

public class ServerException extends CustomException{
    public ServerException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
