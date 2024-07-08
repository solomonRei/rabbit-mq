package org.practice.sender.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String DIRECT_EXCHANGE_NAME = "trx-events-direct";
    public static final String QUEUE_NAME = "my_quorum_queue";
    public static final String ROUTING_KEY = "my_routing_key";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue quorumQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "quorum");
        return new Queue(QUEUE_NAME, true, false, false, args);
    }

    @Bean
    public Binding binding(Queue quorumQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(quorumQueue).to(directExchange).with(ROUTING_KEY);
    }
}

