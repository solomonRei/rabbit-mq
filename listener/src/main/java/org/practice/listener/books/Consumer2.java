package org.practice.listener.books;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer2 {

    @RabbitListener(queues = "consumer2_queue")
    public void processVipBookings(String message) {
        System.out.println("Consumer1 received VIP books: " + message);
    }
}

