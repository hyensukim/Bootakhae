package com.bootakhae.webapp.user.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUser {

    private String resEmail;

    private String resPassword;

    private String resAddress1;

    private String resAddress2;

    private String resName;

    private String resNickname;

    private String resPhone;

    private String resUserId;

    private LocalDateTime resCreatedAt;

}
