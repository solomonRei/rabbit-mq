package org.practice.sender.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Public Exchanges
    public static final String BOOKINGS_NEW_EXCHANGE = "bookings_new";
    public static final String BOOKINGS_MODIFIED_EXCHANGE = "bookings_modified";

    // Consumer specific private exchanges
    public static final String CONSUMER1_PRIVATE_EXCHANGE = "consumer1_private";
    public static final String CONSUMER2_PRIVATE_EXCHANGE = "consumer2_private";
    public static final String CONSUMER3_PRIVATE_EXCHANGE = "consumer3_private";

    // Consumer specific queues
    public static final String CONSUMER1_QUEUE = "consumer1_queue";
    public static final String CONSUMER2_QUEUE = "consumer2_queue";
    public static final String CONSUMER3_QUEUE = "consumer3_queue";

    public static final String DIRECT_EXCHANGE_NAME = "trx-events-directs";
    public static final String DLX_EXCHANGE_NAME = "dlx-exchange";
    public static final String QUEUE_NAME = "eos-queue";
    public static final String DLX_QUEUE_NAME = "dlx-queue";
    public static final String ROUTING_KEY = "my_stream_key";
    public static final String DLX_ROUTING_KEY = "dlx-routing-key";

    // Exchange and Queue Names for Delayed Retry
    public static final String DELAYED_RETRY_EXCHANGE_L0 = "retry-exchange-level-0";
    public static final String DELAYED_RETRY_EXCHANGE_L1 = "retry-exchange-level-1";
    public static final String DELAYED_RETRY_EXCHANGE_L2 = "retry-exchange-level-2";
    public static final String DELAYED_RETRY_EXCHANGE_L3 = "retry-exchange-level-3";

    public static final String DELAYED_RETRY_QUEUE_L1 = "retry-queue-level-1";
    public static final String DELAYED_RETRY_QUEUE_L2 = "retry-queue-level-2";
    public static final String DELAYED_RETRY_QUEUE_L3 = "retry-queue-level-3";

    public static final String CONSUMER_QUEUE_1 = "consumer-queue-1";
    public static final String CONSUMER_QUEUE_2 = "consumer-queue-2";

    public static final String ROUTING_KEY_L1 = "*.0.#";
    public static final String ROUTING_KEY_L2 = "*.1.#";
    public static final String ROUTING_KEY_L3 = "*.1.1.#";

    @Bean
    public FanoutExchange bookingsNewExchange() {
        return new FanoutExchange(BOOKINGS_NEW_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange bookingsModifiedExchange() {
        return new FanoutExchange(BOOKINGS_MODIFIED_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange consumer1PrivateExchange() {
        return new DirectExchange(CONSUMER1_PRIVATE_EXCHANGE, true, false);
    }

    @Bean
    public HeadersExchange consumer2PrivateExchange() {
        return new HeadersExchange(CONSUMER2_PRIVATE_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange consumer3PrivateExchange() {
        return new TopicExchange(CONSUMER3_PRIVATE_EXCHANGE, true, false);
    }

    @Bean
    public Queue consumer1Queue() {
        return QueueBuilder.durable(CONSUMER1_QUEUE).build();
    }

    @Bean
    public Queue consumer2Queue() {
        return QueueBuilder.durable(CONSUMER2_QUEUE).build();
    }

    @Bean
    public Queue consumer3Queue() {
        return QueueBuilder.durable(CONSUMER3_QUEUE).build();
    }

    @Bean
    public Binding bindConsumer1PrivateExchangeToBookingsNew(FanoutExchange bookingsNewExchange, DirectExchange consumer1PrivateExchange) {
        return BindingBuilder.bind(consumer1PrivateExchange).to(bookingsNewExchange);
    }

    @Bean
    public Binding bindConsumer1QueueToPrivateExchange(Queue consumer1Queue, DirectExchange consumer1PrivateExchange) {
        return BindingBuilder.bind(consumer1Queue).to(consumer1PrivateExchange).with("all_bookings");
    }

    @Bean
    public Binding bindConsumer2PrivateExchangeToBookingsNew(FanoutExchange bookingsNewExchange, HeadersExchange consumer2PrivateExchange) {
        return BindingBuilder.bind(consumer2PrivateExchange).to(bookingsNewExchange);
    }

    @Bean
    public Binding bindConsumer2QueueToPrivateExchange(Queue consumer2Queue, HeadersExchange consumer2PrivateExchange) {
        return BindingBuilder.bind(consumer2Queue).to(consumer2PrivateExchange).where("client.id").matches("vip.client");
    }

    @Bean
    public Binding bindConsumer3PrivateExchangeToBookingsModified(FanoutExchange bookingsModifiedExchange, TopicExchange consumer3PrivateExchange) {
        return BindingBuilder.bind(consumer3PrivateExchange).to(bookingsModifiedExchange);
    }

    @Bean
    public Binding bindConsumer3PrivateExchangeToBookingsNew(FanoutExchange bookingsNewExchange, TopicExchange consumer3PrivateExchange) {
        return BindingBuilder.bind(consumer3PrivateExchange).to(bookingsNewExchange);
    }


    @Bean
    public Binding bindConsumer3QueueToPrivateExchange(Queue consumer3Queue, TopicExchange consumer3PrivateExchange) {
        return BindingBuilder.bind(consumer3Queue).to(consumer3PrivateExchange).with("mytravel.com.#");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue quorumQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-queue-type", "quorum")
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .withArgument("x-message-deduplication", true)
                .withArgument("x-cache-ttl", 60000)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLX_QUEUE_NAME).build();
    }

    @Bean
    public Binding binding(Queue quorumQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(quorumQueue).to(directExchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DLX_ROUTING_KEY);
    }

    @Bean
    public TopicExchange retryExchangeLevel0() {
        return new TopicExchange(DELAYED_RETRY_EXCHANGE_L0, true, false);
    }

    @Bean
    public TopicExchange retryExchangeLevel1() {
        return new TopicExchange(DELAYED_RETRY_EXCHANGE_L1, true, false);
    }

    @Bean
    public TopicExchange retryExchangeLevel2() {
        return new TopicExchange(DELAYED_RETRY_EXCHANGE_L2, true, false);
    }

    @Bean
    public TopicExchange retryExchangeLevel3() {
        return new TopicExchange(DELAYED_RETRY_EXCHANGE_L3, true, false);
    }

    @Bean
    public Queue retryQueueLevel1() {
        return QueueBuilder.durable(DELAYED_RETRY_QUEUE_L1)
                .withArgument("x-message-ttl", 60000)
                .withArgument("x-dead-letter-exchange", DELAYED_RETRY_EXCHANGE_L0)
                .build();
    }

    @Bean
    public Queue retryQueueLevel2() {
        return QueueBuilder.durable(DELAYED_RETRY_QUEUE_L2)
                .withArgument("x-message-ttl", 120000)
                .withArgument("x-dead-letter-exchange", DELAYED_RETRY_EXCHANGE_L1)
                .build();
    }

    @Bean
    public Queue retryQueueLevel3() {
        return QueueBuilder.durable(DELAYED_RETRY_QUEUE_L3)
                .withArgument("x-message-ttl", 240000)
                .withArgument("x-dead-letter-exchange", DELAYED_RETRY_EXCHANGE_L2)
                .build();
    }

    @Bean
    public Queue consumerQueue1() {
        return QueueBuilder.durable(CONSUMER_QUEUE_1)
                .build();
    }

    @Bean
    public Queue consumerQueue2() {
        return QueueBuilder.durable(CONSUMER_QUEUE_2)
                .build();
    }

    @Bean
    public Binding bindRetryQueueLevel1(Queue retryQueueLevel1, TopicExchange retryExchangeLevel1) {
        return BindingBuilder.bind(retryQueueLevel1).to(retryExchangeLevel1).with(ROUTING_KEY_L1);
    }

    @Bean
    public Binding bindRetryQueueLevel2(Queue retryQueueLevel2, TopicExchange retryExchangeLevel2) {
        return BindingBuilder.bind(retryQueueLevel2).to(retryExchangeLevel2).with(ROUTING_KEY_L2);
    }

    @Bean
    public Binding bindRetryQueueLevel3(Queue retryQueueLevel3, TopicExchange retryExchangeLevel3) {
        return BindingBuilder.bind(retryQueueLevel3).to(retryExchangeLevel3).with(ROUTING_KEY_L3);
    }

    @Bean
    public Binding bindConsumerQueue1(Queue consumerQueue1, TopicExchange retryExchangeLevel0) {
        return BindingBuilder.bind(consumerQueue1).to(retryExchangeLevel0).with("#.consumer.app.1");
    }

    @Bean
    public Binding bindConsumerQueue2(Queue consumerQueue2, TopicExchange retryExchangeLevel0) {
        return BindingBuilder.bind(consumerQueue2).to(retryExchangeLevel0).with("#.consumer.app.2");
    }
}
