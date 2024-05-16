package com.bootakhae.orderservice.resilience4j_test;

import com.bootakhae.orderservice.global.clients.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Resilience4j 테스트를 위한 코드입니다.
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorService {
    private final UserClient userClient;

//    @CircuitBreaker(name = "cb-case", fallbackMethod = "fallbackMethod1")
    @Retry(name = "rt-case", fallbackMethod = "fallbackRetry")
    public String errorTest1(){
        return userClient.case1();
    }

    @CircuitBreaker(name = "cb-case")
    public String errorTest2(){
        return userClient.case2();
    }

    @CircuitBreaker(name = "cb-case")
    public String errorTest3(){
        return userClient.case3();
    }

    private String fallbackMethod1(Throwable throwable){
        return "[fallback] : " + throwable.getMessage();
    }

    private String fallbackRetry(Exception e){
        return "[retry-fallback] : " + e.getMessage();
    }
}
