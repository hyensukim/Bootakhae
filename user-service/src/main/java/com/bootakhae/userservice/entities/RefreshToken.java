package com.bootakhae.userservice.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash(value="jwtToken", timeToLive = 60 * 60 * 24)
public class RefreshToken {
    @Id
    private String id; // Redis Key 값

    private String refreshToken;
}
