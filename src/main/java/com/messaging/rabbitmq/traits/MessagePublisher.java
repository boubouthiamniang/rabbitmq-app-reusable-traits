package com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;

public interface MessagePublisher {

    default void publish(Channel channel, String exchange, String routingKey, String message) throws Exception {
        channel.basicPublish(exchange, routingKey, null, message.getBytes());
    }
}
