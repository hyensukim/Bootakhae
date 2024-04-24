package com.bootakhae.userservice.global.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private HttpStatus status;
    private String code;
    private String message;
    private String timeStamp;
}
