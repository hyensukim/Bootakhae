package com.bootakhae.gatewayserver.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private HttpStatus status;
    private String code;
    private String message;
    private LocalDateTime timestamp;

    private ErrorResponse(CustomException e){
        ErrorCode error = e.getErrorCode();
        this.status = error.getStatus();
        this.code = error.getCode();
        this.message = error.getMessage();
        this.timestamp = error.getTimestamp();
    }

    private ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode errorCode){return new ErrorResponse(errorCode);}
    public static ErrorResponse of(CustomException e){
        return new ErrorResponse(e);
    }
    public static Mono<Void> onError(ServerWebExchange exchange, ErrorResponse errorResponse) {
        ServerHttpResponse response = exchange.getResponse();

        // 오류 상태코드 작성
        response.setStatusCode(errorResponse.getStatus());

        // 오류 응답 바디 작성
        try {
            byte[] errorByte = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsBytes(errorResponse);

            DataBuffer dataBuffer = response.bufferFactory().wrap(errorByte);
            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            return response.setComplete();
        }
    }
}
