package com.bootakhae.orderservice.errorTest;

import com.bootakhae.orderservice.global.clients.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
