package org.practice.listener.books;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBooks {
    @RabbitListener(queues = "consumer1_queue")
    public void processAllBookings(String message) {
        System.out.println("Consumer1 received: " + message);
    }

    @RabbitListener(queues = "consumer2_queue")
    public void processVipBookings(String message) {
        System.out.println("Consumer1 received VIP books: " + message);
    }

    @RabbitListener(queues = "consumer3_queue")
    public void processModifiedBookings(String message) {
        System.out.println("Consumer1 received Modified Booking: " + message);
    }
}
