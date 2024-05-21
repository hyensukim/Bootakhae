package com.bootakhae.userservice.services;

import com.bootakhae.userservice.dto.TokenDto;
import com.bootakhae.userservice.entities.RefreshToken;
import com.bootakhae.userservice.global.exception.CustomException;
import com.bootakhae.userservice.global.exception.ErrorCode;
import com.bootakhae.userservice.global.security.TokenProvider;
import com.bootakhae.userservice.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void saveTokenInfo(String userId, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));
    }

    @Override
    public void removeTokenInfo(String userId) {
        refreshTokenRepository.findById(userId).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public TokenDto  findAndValidate(String userId) {
        RefreshToken refreshTokenInfo = refreshTokenRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN)
        );

        String refreshToken = refreshTokenInfo.getRefreshToken();
        if(!tokenProvider.validateRefreshToken(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String roles = tokenProvider.getRolesByToken(refreshToken);

        String newAccessToken = tokenProvider.createAccessToken(userId, roles);
        String newRefreshToken = tokenProvider.createRefreshToken(roles);

        return new TokenDto(userId, newAccessToken, newRefreshToken);
    }
}
