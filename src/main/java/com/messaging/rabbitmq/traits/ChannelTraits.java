package com.messaging.rabbitmq.traits;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public interface ChannelTraits {

    default Channel createChannel(Connection connection) throws Exception {
        return connection.createChannel();
    }  

    default void channelBasicQos(Channel channel, int prefetchCount) throws IOException {
        channel.basicQos(prefetchCount);
    }

    default void channelBasicAck(Channel channel, long deliveryTag, boolean multiple) throws IOException {
        channel.basicAck(deliveryTag, multiple);
    }

    default void channelBasicNack(Channel channel, long deliveryTag, boolean multiple, boolean requeue) throws IOException {
        channel.basicNack(deliveryTag, multiple, requeue);  
    }

    default void channelBasicReject(Channel channel, long deliveryTag, boolean requeue) throws IOException {
        channel.basicReject(deliveryTag, requeue);   
    }

    default void channelBasicCancel(Channel channel, String consumerTag) throws IOException {
        channel.basicCancel(consumerTag);   
    } 

    default void channelQueueBind(Channel channel, String queueName, String exchangeName, String routingKey) throws IOException {
        channel.queueBind(queueName, exchangeName, routingKey);  
    } 

    default void channelConfirmSelect(Channel channel) throws IOException {
        channel.confirmSelect();
    }
    
}
