package com.bootakhae.gatewayserver.global.util;

//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.core.ReactiveValueOperations;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
////@Service
//public class RedisUtil {
//    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
//
//    public RedisUtil(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
//        this.reactiveRedisTemplate = reactiveRedisTemplate;
//    }
//
//    public Mono<String> getValue(String key) {
//        ReactiveValueOperations<String, String> operations = reactiveRedisTemplate.opsForValue();
//        return operations.get(key);
//    }
//}
