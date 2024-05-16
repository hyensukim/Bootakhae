package com.bootakhae.gatewayserver.global.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
}
