//package org.practice.listener;
//
//import com.rabbitmq.client.*;
//import lombok.extern.slf4j.Slf4j;
//import org.practice.listener.config.RabbitMQConfig;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Argument;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class StreamConsumer implements ChannelAwareMessageListener {
//
//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange(type = ExchangeTypes.DIRECT, name = RabbitMQConfig.DIRECT_EXCHANGE_NAME),
//            value = @Queue(value = RabbitMQConfig.QUEUE_NAME, durable = "true", arguments = @Argument(name = "x-queue-type", value = "quorum")),
//            key = RabbitMQConfig.ROUTING_KEY
//    ), ackMode = "MANUAL")
//    public void onDirectMessage(Message message, Channel channel) throws Exception {
//        log.info("Message received: {}", new String(message.getBody()));
//
//        try {
//            // Симуляция обработки
//            processMessage(new String(message.getBody()));
//
//            // Подтверждаем успешную обработку
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            log.info("Message processed and acknowledged: {}", new String(message.getBody()));
//        } catch (Exception e) {
//            log.error("Processing failed", e);
//
//            // Отправляем сообщение обратно в очередь
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        }
//    }
//
//    private void processMessage(String body) throws InterruptedException {
//        log.info("Processing message: {}", body);
//        if ("END".equals(body)) {
//            log.info("End of Stream detected. Stopping processing.");
//            throw new RuntimeException("EOS detected");
//        }
//        // Имитируем задержку обработки
//        Thread.sleep(500);
//    }
//
//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//        onDirectMessage(message, channel);
//    }
//}
