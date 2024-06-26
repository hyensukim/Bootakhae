package com.bootakhae.webapp.user.dto;

import com.bootakhae.webapp.user.constant.Role;
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
