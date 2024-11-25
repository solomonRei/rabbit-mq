package org.practice.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final MessagePublisher messagePublisher;
    private final AtomicInteger counter = new AtomicInteger(1);

//    @Scheduled(fixedRate = 100)
    public void sendMessage() {
        String message = "Hello, RabbitMQ! Message number: " + counter.getAndIncrement();
        messagePublisher.publishMessage(message);
    }
}
