package com.bootakhae.userservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToken {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
