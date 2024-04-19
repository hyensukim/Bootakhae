package com.bootakhae.webapp.user.dto;

import lombok.*;

//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class AuthInfoDto {
    private UserDto userDetails;
    private String accessToken;
    private String refreshToken;
}
