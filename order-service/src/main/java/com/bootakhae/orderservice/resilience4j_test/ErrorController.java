package com.bootakhae.orderservice.resilience4j_test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Resilience4j 테스트를 위한 코드입니다.
 */

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class ErrorController {

    private final ErrorService errorService;

    @GetMapping("case1")
    public ResponseEntity<?> test1(){
        String result = errorService.errorTest1();

        return ResponseEntity.ok(result);
    }

    @GetMapping("case2")
    public ResponseEntity<?> test2(){
        String result = errorService.errorTest2();

        return ResponseEntity.ok(result);
    }

    @GetMapping("case3")
    public ResponseEntity<?> test3(){
        String result = errorService.errorTest3();

        return ResponseEntity.ok(result);
    }
}
