package com.messaging.rabbitmq.traits;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public interface ChannelTraits {

    default public Channel createChannel(Connection connection) throws Exception {
        return connection.createChannel();
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

    default Map<String, Object> createQueueArguments(String type, Integer timeToLive, Integer lengthLimit, Integer redeliveryLimit, Integer maxNbPriorities) {
        Map<String, Object> arguments = new HashMap<>();

        // Default values
        String defaultType = "classic"; // Default queue type (alternative quorum)
        int defaultTTL = 60000; // 60 seconds
        int defaultLengthLimit = 1000; // Max 1000 messages
        int defaultRedeliveryLimit = 5; // Allow 5 redeliveries
        int defaultMaxPriority = 10; // Max priority of 10

        // Set queue type (classic, quorum, etc.)
        arguments.put("x-queue-type", (type != null && !type.isEmpty()) ? type : defaultType);

        // Set max priority
        arguments.put("x-max-priority", (maxNbPriorities != null && maxNbPriorities > 0) ? maxNbPriorities : defaultMaxPriority);

        // Set message TTL (time-to-live)
        arguments.put("x-message-ttl", (timeToLive != null && timeToLive > 0) ? timeToLive : defaultTTL);

        // Set max queue length
        arguments.put("x-max-length", (lengthLimit != null && lengthLimit > 0) ? lengthLimit : defaultLengthLimit);

        // Set dead-letter exchange for redelivery handling
        if (redeliveryLimit == null || redeliveryLimit <= 0) {
            redeliveryLimit = defaultRedeliveryLimit;
        }
        arguments.put("x-dead-letter-exchange", "dlx-exchange"); // Default dead-letter exchange
        arguments.put("x-dead-letter-routing-key", "dlx-routing"); // Default routing key

        return arguments;
    }
}
