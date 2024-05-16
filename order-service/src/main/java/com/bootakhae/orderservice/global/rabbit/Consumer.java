package com.bootakhae.orderservice.global.rabbit;

import com.bootakhae.orderservice.order.services.OrderService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    @RabbitListener(queues = "ORDER_SERVICE_QUEUE")
    public void receiveMessage(String message) throws IOException {
        log.debug("이벤트 consuming : {}", message);

        EventMessage eventMessage = objectMapper.readValue(message, EventMessage.class);

        if("SHIPPING".equals(eventMessage.getEvent())){
            orderService.changeOrderStatusAfterPayment();
        }
        else if("DONE".equals(eventMessage.getEvent())){
            orderService.changeOrderStatusAfterShipping();
        }
        else if("RETURN".equals(eventMessage.getEvent())){
            orderService.changeOrderStatusForReturn();
        }
    }
}
