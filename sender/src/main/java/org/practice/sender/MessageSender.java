package org.practice.sender;

import lombok.AllArgsConstructor;
import org.practice.sender.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
@AllArgsConstructor
public class MessageSender {

    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public String sendMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String message = "Hello, RabbitMQ! " + i;
            messageBuilder.append(message).append("\n");
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        }
        return messageBuilder.toString();
    }
}
