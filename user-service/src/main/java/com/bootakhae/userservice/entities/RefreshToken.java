package com.bootakhae.userservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value="refreshToken", timeToLive = 60 * 60 * 24)
public class RefreshToken {

    @Id
    private String id; // Redis Key 값

    private String refreshToken;
}
