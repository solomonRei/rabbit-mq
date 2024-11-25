package org.practice.listener.books;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer3 {

    @RabbitListener(queues = "consumer3_queue")
    public void processVipBookings(String message) {
        System.out.println("Consumer1 received Modified Booking: " + message);
    }
}

