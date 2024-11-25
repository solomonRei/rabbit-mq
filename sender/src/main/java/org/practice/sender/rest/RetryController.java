package org.practice.sender.rest;

import lombok.AllArgsConstructor;
import org.practice.sender.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retry")
@AllArgsConstructor
public class RetryController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message, @RequestParam int delayInMinutes) {
        String routingKey;

        if (delayInMinutes <= 1) {
            routingKey = "*.0.#";
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_RETRY_EXCHANGE_L1, routingKey, message);
        } else if (delayInMinutes <= 2) {
            routingKey = "*.1.#";
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_RETRY_EXCHANGE_L2, routingKey, message);
        } else {
            routingKey = "*.1.1.#";
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_RETRY_EXCHANGE_L3, routingKey, message);
        }

        return "Message sent: " + message + " with delay: " + delayInMinutes + " minutes.";
    }
}

