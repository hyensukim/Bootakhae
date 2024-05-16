package com.bootakhae.userservice.global.utils;

import com.bootakhae.userservice.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageUtil {

    public String writeMessage(ErrorCode error){
        StringBuilder errMsg = new StringBuilder();
        errMsg.append("status : ").append(error.getStatus().name()).append("\n");
        errMsg.append("message : ").append(error.getMessage()).append("\n");
        errMsg.append("code : ").append(error.getCode()).append("\n");
        errMsg.append("timestamp : ").append(error.getTimestamp()).append("\n");
        return errMsg.toString();
    }
}
