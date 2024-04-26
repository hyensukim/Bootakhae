package com.bootakhae.productservice.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorAdvisor {
    /**
     * 유효성 검증 에러 응답 처리
     */
    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<?> handleValidationError(BindException e){
        log.error("[Validation] 유효성 검증 에러 발생 : ", e);
        List<String> errorMessageList = e.getFieldErrors().stream().map(
                b->b.getField() + " : " + b.getDefaultMessage()
        ).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessageList);
    }

    /**
     * Custom 에러 응답 처리
     */
    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<?> handleGlobalError(CustomException e){
        log.error("[Custom Error] 예외 발생 : ",e);
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e));
    }

    /**
     * 전체 에러 응답 처리
     */
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<?> handleGlobalError(Exception e){
        log.error("[Error] 예외 발생 : ",e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
