package com.bootakhae.userservice.dto;

import com.bootakhae.userservice.vo.response.ResponseToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String userId;
    private String accessToken;
    private String refreshToken;

    public ResponseToken dtoToVo(){
        return ResponseToken.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
