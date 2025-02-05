package main.java.com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public interface SubscriberTraits {

    default DeliverCallback createMessageHandler() {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
    }

    public default void consumeMessages(Channel channel, String queueName, DeliverCallback callback) throws Exception {
        channel.basicConsume(queueName, true, callback, consumerTag -> {});
    }
}
