package org.practice.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterMessageListener {

    @RabbitListener(queues = "dlx-queue", ackMode = "MANUAL", concurrency = "1")
    public void onDeadLetterMessage(Message message, Channel channel) {
        String threadName = Thread.currentThread().getName();
        String messageBody = new String(message.getBody());
        String messageId = message.getMessageProperties().getMessageId();

        try {
            System.out.println("Message Body: " + messageBody);
            processDeadLetterMessage(message);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("Thread: " + threadName + " - Dead Letter Message processed and acknowledged.");
        } catch (Exception e) {
            System.err.println("Thread: " + threadName + " - Error processing Dead Letter Message: " + messageId);
            e.printStackTrace();

            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                System.err.println("Thread: " + threadName + " - Dead Letter Message nacked and retained in DLQ.");
            } catch (Exception nackException) {
                System.err.println("Thread: " + threadName + " - Failed to nack Dead Letter Message.");
                nackException.printStackTrace();
            }
        }
    }

    private void processDeadLetterMessage(Message message) {
        System.out.println("Processing Dead Letter: " + new String(message.getBody()));
    }
}
