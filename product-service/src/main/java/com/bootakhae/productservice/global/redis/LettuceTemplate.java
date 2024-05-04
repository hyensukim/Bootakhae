package com.bootakhae.productservice.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class LettuceTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(String productId) {
        // 레디스 내부에 키값이 존재하지 않을 경우 3초 유효한 새로 만들어주는 명령어 - Lock 점유 성공 시 true, 점유 실패 시 false
        return redisTemplate.opsForValue().setIfAbsent(productId, "lock", Duration.ofMillis(3_000));
    }

    public void unlock(String productId) {
        // 특정 키값을 지우기
        redisTemplate.delete(productId);
    }

}
