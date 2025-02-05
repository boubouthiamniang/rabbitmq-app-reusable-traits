package com.messaging.rabbitmq.traits;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public interface ChannelTraits {

    default public Channel createChannel(Connection connection) throws Exception {
        return connection.createChannel();
    }

    //Exchange
    default void declareExchange(String exchangeName, String exchangeType) throws Exception {
        channel.exchangeDeclare(exchangeName, exchangeType);
    }
    
    //Without argument
    default void declareQueue(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    } 

    default void declareQueueDurable(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    } 

    default void declareQueueExclusive(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, false, true, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    } 

    default void declareQueueAutoDelete(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, false, false, true, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default void declareQueueDurableExclusive(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, true, true, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default void declareQueueDurableAutoDelete(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, true, false, true, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default void declareQueueDurableExclusiveAutoDelete(Channel channel, String queueName) throws Exception {
        channel.queueDeclare(queueName, true, true, true, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }
    
    //With arguments
    default void declareQueue(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, false, false, false, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    } 

    default void declareQueueDurable(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, true, false, false, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    } 

    default void declareQueueExclusive(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, false, true, false, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    } 

    default void declareQueueAutoDelete(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, false, false, true, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default void declareQueueDurableExclusive(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, true, true, false, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default void declareQueueDurableAutoDelete(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, true, false, true, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default void declareQueueDurableExclusiveAutoDelete(Channel channel, String queueName, Map<String, Object> queueArgs) throws Exception {
        channel.queueDeclare(queueName, true, true, true, queueArgs);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    default Map<String, Object> createQueueArguments(
        String type,
        Integer timeToLive,
        Integer lengthLimit,
        Integer redeliveryLimit,
        Integer maxNbPriorities) {
        
        return new QueueArgumentsBuilder()
            .setType(type != null ? type : "classic")
            .setTimeToLive(timeToLive != null ? timeToLive : 60000)
            .setLengthLimit(lengthLimit != null ? lengthLimit : 1000)
            .setRedeliveryLimit(redeliveryLimit != null ? redeliveryLimit : 5)
            .setMaxNbPriorities(maxNbPriorities != null ? maxNbPriorities : 10)
            .build();
    }
}
