package org.practice.listener.books;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer1 {

    @RabbitListener(queues = "consumer1_queue")
    public void processAllBookings(String message) {
        System.out.println("Consumer1 received: " + message);
    }
}

