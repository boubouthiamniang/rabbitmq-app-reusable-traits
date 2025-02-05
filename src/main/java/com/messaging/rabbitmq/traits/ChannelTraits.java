package com.messaging.rabbitmq.traits;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public interface ChannelTraits {

    default public Channel createChannel(Connection connection) throws Exception {
        return connection..createChannel();
    }  

    default void channelBasicQos(Channel channel, int prefetchCount) {
        channel.basicQos(prefetchCount);
    }

    default void channelBasicAck(Channel channel, DeliverCallback delivery) {
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }

    default void channelBasicNack(Channel channel, long deliveryTag, boolean multiple, boolean requeue) {
        channel.basicNack(deliveryTag, multiple, requeue);  
    }

    default void channelBasicReject(Channel channel, long deliveryTag, boolean requeue) {
        channel.basicReject(deliveryTag, requeue);   
    }
}
