package com.bootakhae.userservice.dto;

import com.bootakhae.userservice.global.constant.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String password;
    private String address1;
    private String address2;
    private String name;
    private String nickname;
    private String phone;
    private String userId;
    private Role role;
    private LocalDateTime createdAt;
}
