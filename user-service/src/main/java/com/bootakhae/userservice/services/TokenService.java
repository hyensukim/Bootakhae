package com.bootakhae.userservice.services;

import com.bootakhae.userservice.dto.TokenDto;

public interface TokenService{
    void saveRefreshToken(String userId, String refreshToken);

    void removeRefreshToken(String refreshToken);

    TokenDto reIssueToken(String refreshToken);

    TokenDto findTokenByRefreshToken(String refreshToken);

    boolean haveRefreshToken(String refreshToken);
}
