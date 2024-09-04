package com.bootakhae.orderservice.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeTest {

    @Disabled
    @Test
    @DisplayName("시간 차이를 구하는 로직 테스트")
    public void 시간차이확인(){
        // 현재 시간 2024.05.11 01:53
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime before = LocalDateTime.of(2024,5,11,0,49);

        Duration duration = Duration.between(now, before);

        long hour = Math.abs(duration.toHours());

        assertEquals(1, hour);
    }
}
