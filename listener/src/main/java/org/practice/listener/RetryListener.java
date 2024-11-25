package org.practice.listener;

import org.practice.listener.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RetryListener {

    @RabbitListener(queues = RabbitMQConfig.CONSUMER_QUEUE_1)
    public void handleMessageConsumer1(@Payload String message) {
        System.out.println("Consumer 1 received message: " + message);

        if (message.contains("retry")) {
            System.out.println("Consumer 1 failed to process the message. Retrying...");
            throw new RuntimeException("Simulated failure");
        }
        System.out.println("Consumer 1 successfully processed the message.");
    }


    @RabbitListener(queues = RabbitMQConfig.CONSUMER_QUEUE_2)
    public void handleMessageConsumer2(@Payload String message) {
        System.out.println("Consumer 2 received message: " + message);

        System.out.println("Consumer 2 successfully processed the message.");
    }
}
