package org.practice.sender.rest;

import lombok.AllArgsConstructor;
import org.practice.sender.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/send")
@AllArgsConstructor
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/batch")
    public String sendBatchMessages() {
        StringBuilder messageBuilder = new StringBuilder("Sending Batch Messages:\n");

        IntStream.range(1, 101).forEach(i -> {
            String message = "Batch Event " + i;
            messageBuilder.append(message).append("\n");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DIRECT_EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    message
            );
        });

        return messageBuilder.toString();
    }
}

