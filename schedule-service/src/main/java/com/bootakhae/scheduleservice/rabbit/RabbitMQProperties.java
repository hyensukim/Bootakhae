package com.bootakhae.scheduleservice.rabbit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
@AllArgsConstructor
@Getter
public class RabbitMQProperties {
    private String username;
    private String password;
    private String host;
    private int port;
}
