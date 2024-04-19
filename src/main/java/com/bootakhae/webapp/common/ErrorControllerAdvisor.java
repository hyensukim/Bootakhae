package com.bootakhae.webapp.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvisor {

    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<?> handleValidationError(BindException e){
        log.error("유효성 검증 에러 발생 : ", e);
        List<String> errorMessageList = e.getFieldErrors().stream().map(
                b->b.getField() + " : " + b.getDefaultMessage()
        ).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessageList);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<?> handleGlobalError(Exception e){
        log.error("예외 발생 : ",e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
