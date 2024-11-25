package org.practice.sender.rest;


import lombok.AllArgsConstructor;
import org.practice.sender.BookingPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BookingController {

    private final BookingPublisher bookingPublisher;

    @GetMapping("/publish/new")
    public String publishNewBooking(@RequestParam String message, @RequestParam(defaultValue = "false") boolean isVip) {
        bookingPublisher.publishNewBooking(message, isVip);
        return "Published new booking: " + message + (isVip ? " [VIP]" : "");
    }

    @GetMapping("/publish/modified")
    public String publishModifiedBooking(@RequestParam String message) {
        bookingPublisher.publishModifiedBooking(message);
        return "Published modified booking: " + message;
    }
}

