package com.messaging.rabbitmq.traits;

import java.io.IOException;
import java.util.Map;

import com.messaging.rabbitmq.utils.QueueArgumentsBuilder;
import com.rabbitmq.client.Channel;

public interface QueueTraits {

    default void declareQueueMinimal(Channel channel) throws Exception {
        channel.queueDeclare();
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }
 
    default void declareQueue(Channel channel, String queueName, boolean isDurable, boolean isExclusive, boolean isAutoDelete, Map<String, Object> queueArguments) throws Exception {
        channel.queueDeclare(queueName, isDurable, isExclusive, isAutoDelete, queueArguments);
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

    
    default void purgeQueue(Channel channel, String queueName) throws IOException {
        channel.queuePurge(queueName);
    }
}
