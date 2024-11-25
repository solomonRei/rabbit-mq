package org.practice.sender;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class BookingPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishNewBooking(String message, boolean isVip) {
        if (isVip) {
            rabbitTemplate.convertAndSend("bookings_new", "",
                    MessageBuilder.withBody(message.getBytes())
                            .setHeader("client.id", "vip.client")
                            .build());
        } else {
            System.out.println("SENDING TO bookings_new");
            rabbitTemplate.convertAndSend("bookings_new", "all_bookings", message);
        }

        if (message.contains("mytravel.com")) {
            System.out.println("SENDING TO bookings_new - mytravel.com.new");
            rabbitTemplate.convertAndSend("bookings_new", "mytravel.com.new", message);
        }

        System.out.println("Published to bookings_new: " + message + (isVip ? " [VIP]" : ""));
    }

    public void publishModifiedBooking(String message) {
        rabbitTemplate.convertAndSend("bookings_modified", "", message);

        if (message.contains("mytravel.com")) {
            rabbitTemplate.convertAndSend("bookings_modified", "mytravel.com.modified", message);
        }

        System.out.println("Published to bookings_modified: " + message);
    }
}

