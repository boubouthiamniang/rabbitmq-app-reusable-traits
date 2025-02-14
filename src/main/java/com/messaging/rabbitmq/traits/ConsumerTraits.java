package com.messaging.rabbitmq.traits;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public interface ConsumerTraits {

    public default void consume(Channel channel, String queueName, boolean autoAck) throws Exception {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
    
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> { });
    }

    default void consumeCompeting(Channel channel, Delivery delivery) throws UnsupportedEncodingException {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("Received: " + message);

        try {
            // Simulate work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                // Acknowledge the message
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //To overridde
    default void doWork(String task) {}
}
