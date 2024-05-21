package com.bootakhae.scheduleservice.services;

import com.bootakhae.scheduleservice.rabbit.EventMessage;
import com.bootakhae.scheduleservice.rabbit.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ObjectMapper objectMapper;

    private final Producer producer;

    /**
     * 매일 낮 12시에 확인
     */
    @Scheduled(cron = "${schedule.order.cron}")
    public void changeOrderStatus() {
        log.debug("주문 상태 업데이트 실행");

        log.debug("배송 중 변경 이벤트 발생!");
        convertAndSendToOrder(new EventMessage(("SHIPPING")));

        log.debug("배송 완료 변경 이벤트 발생!");
        convertAndSendToOrder(new EventMessage(("DONE")));

        log.debug("반품 완료 변경 이벤트 발생!");
        convertAndSendToOrder(new EventMessage(("RETURN")));
    }

    /**
     * 매일 오후 2시에 이벤트 상품 확인
     */
    @Scheduled(cron = "${schedule.product.cron}")
    public void changeEventOpenFlag() {
        log.debug("이벤트 상태 업데이트 실행");

        convertAndSendToProduct(new EventMessage(("EVENT! OPEN")));
    }

    private <T> void convertAndSendToOrder(T dto){
        String message = null;
        try {
            message = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        producer.sendMessageToOrderService(message);
    }

    private <T> void convertAndSendToProduct(T dto){
        String message = null;
        try {
            message = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        producer.sendMessageToProductService(message);
    }
}
