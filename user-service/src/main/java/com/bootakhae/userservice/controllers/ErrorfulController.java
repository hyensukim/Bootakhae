package com.bootakhae.userservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Random;

//@RestController
@Slf4j
//@RequestMapping("test/internal")
public class ErrorfulController {

    // 임계값 테스트 - 실패율
    @GetMapping("/errorful/case1")
    public ResponseEntity<String> case1() {
        // Simulate 5% chance of 500 error 랜덤 중 100 중 5가 나올 확률을 시뮬레이팅
        int num = new Random().nextInt(100);
        if (num < 100) {
            System.out.println("num : " + num);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
        log.info("===========================>> pass");
        return ResponseEntity.ok("Normal response");
    }

    // 임계값 테스트 - 느린 호출율
    @GetMapping("/errorful/case2")
    public ResponseEntity<String> case2() {
        // Simulate blocking requests every first 10 seconds
        LocalTime currentTime = LocalTime.now();
        int currentSecond = currentTime.getSecond();

        if (currentSecond < 10) {
            System.out.println("currentSecond : " + currentSecond);
            // Simulate a delay (block) for 10 seconds
            try {
                System.out.println("===========================================" + System.currentTimeMillis());
                Thread.sleep(10000);
                System.out.println("===========================================" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return ResponseEntity.status(503).body("Service Unavailable");
        }

        return ResponseEntity.ok("Normal response");
    }

    // 슬라이딩 윈도우(시간 기반) - 10초 이내에 오류가 발생하는 경우
    @GetMapping("/errorful/case3")
    public ResponseEntity<String> case3() {
        // Simulate 500 error every first 10 seconds
        LocalTime currentTime = LocalTime.now();
        int currentSecond = currentTime.getSecond();

        if (currentSecond < 10) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }

        return ResponseEntity.ok("Normal response");
    }
}
