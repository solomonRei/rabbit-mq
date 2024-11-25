package org.practice.sender.rest;

import lombok.AllArgsConstructor;
import org.practice.sender.ParallelBatchSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producers")
@AllArgsConstructor
public class ProducerController {

    private final ParallelBatchSender parallelBatchSender;

    @GetMapping("/start")
    public String startProducers() {
        parallelBatchSender.startSenders(10);
        return "Started 10 Producers";
    }
}

