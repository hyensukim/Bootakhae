package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.exception.CustomException;
import com.bootakhae.orderservice.global.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // HTTP 상태 코드를 체크하여 사용자 정의 예외 처리
        if (response.status() >= 400 && response.status() <= 499) {
            return new CustomException(ErrorCode.FEIGN_CLIENT_ERROR, makeMessage(response));
        }
        if (response.status() >= 500 && response.status() <= 599) {
            return new CustomException(ErrorCode.FEIGN_SERVER_ERROR, makeMessage(response));
        }
        // 기본 오류 디코더를 사용하여 다른 모든 상황 처리
        return defaultDecoder.decode(methodKey, response);
    }

    private String makeMessage(Response response) {
        return "Status : " + response.status();
    }
}
