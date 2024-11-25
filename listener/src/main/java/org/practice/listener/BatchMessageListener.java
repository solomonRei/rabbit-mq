package org.practice.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BatchMessageListener {

    private static final int BATCH_SIZE = 10;
    private final List<Message> batch = new ArrayList<>();

    @RabbitListener(queues = "eos-queue", ackMode = "MANUAL", concurrency = "1")
    public void onMessage(Message message, Channel channel) {
        String threadName = Thread.currentThread().getName();
        try {
            System.out.println("Thread: " + threadName + " --- START MESSAGE PROCESSING ---");

            batch.add(message);

            System.out.println("Thread: " + threadName + " - Received message: " + new String(message.getBody()));

            if (batch.size() >= BATCH_SIZE) {
                System.out.println("Thread: " + threadName + " --- START BATCH PROCESSING ---");

                processBatch(batch);

                long lastDeliveryTag = batch.get(batch.size() - 1).getMessageProperties().getDeliveryTag();
                channel.basicAck(lastDeliveryTag, true);

                batch.clear();

                System.out.println("Thread: " + threadName + " --- END BATCH PROCESSING ---");
            }

            System.out.println("Thread: " + threadName + " --- END MESSAGE PROCESSING ---");
        } catch (Exception e) {
            System.out.println("Thread: " + threadName + " --- ERROR OCCURRED ---");
            sendToDeadLetterQueue(channel, threadName);
        }
    }

    private void processBatch(List<Message> messages) {
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread: " + threadName + " - Processing batch of " + messages.size() + " messages");
        messages.forEach(msg -> System.out.println("Thread: " + threadName + " - Message: " + new String(msg.getBody())));

        if (Math.random() > 0.8) {
            throw new RuntimeException("Simulated processing failure");
        }
    }

    private void sendToDeadLetterQueue(Channel channel, String threadName) {
        try {
            if (!batch.isEmpty()) {
                long lastDeliveryTag = batch.get(batch.size() - 1).getMessageProperties().getDeliveryTag();
                channel.basicNack(lastDeliveryTag, true, false);
                System.out.println("Thread: " + threadName + " - Sent batch to Dead Letter Queue, size: " + batch.size());
                batch.clear();
            }
        } catch (IOException e) {
            System.err.println("Thread: " + threadName + " - Failed to send batch to DLQ");
            e.printStackTrace();
        }
    }
}
