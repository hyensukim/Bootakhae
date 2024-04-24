package com.bootakhae.webapp.user.services;

import com.bootakhae.webapp.user.dto.TokenDto;

public interface TokenService{
    void saveRefreshToken(String userId, String refreshToken);

    void removeRefreshToken(String refreshToken);

    TokenDto reIssueToken(String refreshToken);

    TokenDto findTokenByRefreshToken(String refreshToken);

    boolean haveRefreshToken(String refreshToken);
}
