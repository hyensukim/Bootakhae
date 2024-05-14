package com.bootakhae.scheduleservice.rabbit;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitMQProperties rabbitMQProperties;

    /**
     * RabbitMQ 연동을 위한 ConnectionFactory 빈을 생성하여 반환
     **/
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMQProperties.getHost());
        connectionFactory.setPort(rabbitMQProperties.getPort());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("commonExchange");
    }

    // durable 을 false 로 설정하면, rabbitMQ 재시작 시에 큐가 삭제됨
    @Bean
    public Queue orderServiceQueue(){
        return new Queue("ORDER_SERVICE_QUEUE");
    }

    @Bean
    Queue productServiceQueue(){
        return new Queue("PRODUCT_SERVICE_QUEUE");
    }

    @Bean
    public Binding orderServiceBinding(Queue orderServiceQueue, DirectExchange exchange){
        return BindingBuilder.bind(orderServiceQueue).to(exchange).with("orderRoutingKey");
    }

    @Bean
    Binding productServiceBinding(Queue productServiceQueue, DirectExchange exchange){
        return BindingBuilder.bind(productServiceQueue).to(exchange).with("productRoutingKey");
    }

    /**
     * RabbitTemplate
     * ConnectionFactory 로 연결 후 실제 작업을 위한 Template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
