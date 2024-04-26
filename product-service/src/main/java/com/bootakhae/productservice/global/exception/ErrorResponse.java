package com.bootakhae.productservice.global.exception;

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

    public static ErrorResponse of(CustomException e){
        return new ErrorResponse(e);
    }
}
