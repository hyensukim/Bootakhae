package com.bootakhae.userservice.services;

import com.bootakhae.userservice.dto.TokenDto;
import com.bootakhae.userservice.global.exception.CustomException;
import com.bootakhae.userservice.global.exception.ErrorCode;
import com.bootakhae.userservice.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final StringRedisTemplate stringRedisTemplate;
    private final TokenProvider tokenProvider;
    private final Environment env;

    private static final String REFRESH_TOKEN_PREFIX = "refresh-token:";

    @Override
    public void saveTokenInfo(String userId, String refreshToken) {
        long ttl = Long.parseLong(Objects.requireNonNull(env.getProperty("redis.refresh-token.ttl")));
        stringRedisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + userId, refreshToken,ttl, TimeUnit.SECONDS);
    }

    @Override
    public void removeTokenInfo(String userId) {
        stringRedisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }

    @Override
    public TokenDto  findAndValidate(String userId) {
        String refreshToken = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);

        if(Objects.isNull(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(!tokenProvider.validateRefreshToken(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String roles = tokenProvider.getRolesByToken(refreshToken);

        String newAccessToken = tokenProvider.createAccessToken(userId, roles);
        String newRefreshToken = tokenProvider.createRefreshToken(roles);

        return new TokenDto(userId, newAccessToken, newRefreshToken);
    }
}
