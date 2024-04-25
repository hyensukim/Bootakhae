package com.bootakhae.userservice.services;

import com.bootakhae.userservice.dto.TokenDto;

public interface RefreshTokenService {

    void saveTokenInfo(String userId, String refreshToken);

    void removeTokenInfo(String userId);

    TokenDto findAndValidate(String userId);
}
