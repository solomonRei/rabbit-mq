package org.practice.sender;

import org.practice.sender.rest.MessageController;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
@EnableAsync
public class ParallelBatchSender {

    private final MessageController batchMessageSender;

    public ParallelBatchSender(MessageController batchMessageSender) {
        this.batchMessageSender = batchMessageSender;
    }


    public void startSenders(int numberOfSenders) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(numberOfSenders);

        IntStream.range(0, numberOfSenders).forEach(i -> {
            executorService.scheduleAtFixedRate(() -> {
                System.out.println("Sender " + (i + 1) + " is sending batch...");
                batchMessageSender.sendBatchMessages();
            }, 0, 1, TimeUnit.SECONDS);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }
}

