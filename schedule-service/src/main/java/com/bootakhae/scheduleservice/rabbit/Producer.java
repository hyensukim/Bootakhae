package com.bootakhae.scheduleservice.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessageToOrderService(String message){
        rabbitTemplate.convertAndSend("commonExchange","orderRoutingKey",message);
    }

    public void sendMessageToProductService(String message){
        rabbitTemplate.convertAndSend("commonExchange","productRoutingKey",message);
    }
}
