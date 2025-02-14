package com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

public interface ProducerTraits {

    default void publish(Channel channel, String exchange, String routingKey, String message) throws Exception {
        channel.basicPublish(exchange, routingKey, null, message.getBytes());
    }

    default void publishWithBasicProperties(Channel channel, String exchange, String routingKey, BasicProperties props, String message) throws Exception {
        channel.basicPublish(exchange, routingKey, props, message.getBytes());
    }
}
