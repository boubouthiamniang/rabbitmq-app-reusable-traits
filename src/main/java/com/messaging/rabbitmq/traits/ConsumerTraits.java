package com.messaging.rabbitmq.traits;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public interface ConsumerTraits {

    public default void consume(Channel channel, String queueName, boolean isAutoAck) throws Exception {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
    
        channel.basicConsume(queueName, isAutoAck, deliverCallback, consumerTag -> { });
    }

    default void consumeCompeting(Channel channel,  String queueName, boolean isAutoAck) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
    
            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, isAutoAck, deliverCallback, consumerTag -> { });
    }

    //To overridde
    public default void doWork(String task) {}
}
