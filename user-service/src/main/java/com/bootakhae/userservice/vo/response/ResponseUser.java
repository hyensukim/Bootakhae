package com.bootakhae.userservice.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUser {

    private String email;

    private String password;

    private String address1;

    private String address2;

    private String name;

    private String nickname;

    private String phone;

    private String userId;

    private LocalDateTime createdAt;

}
