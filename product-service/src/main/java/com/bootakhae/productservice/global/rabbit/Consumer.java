package com.bootakhae.productservice.global.rabbit;

import com.bootakhae.productservice.services.ProductService;
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

    private final ProductService productService;

    @RabbitListener(queues = "PRODUCT_SERVICE_QUEUE")
    public void receiveMessage(String message) throws IOException {
        log.debug("이벤트 consuming : {}", message);
        EventMessage eventMessage = objectMapper.readValue(message, EventMessage.class);

        log.debug("event : {}", eventMessage.getEvent());
        productService.openEventProduct();
    }
}
