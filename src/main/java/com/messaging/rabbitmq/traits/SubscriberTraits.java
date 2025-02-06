package main.java.com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public interface SubscriberTraits {

    public default void consume(Channel channel, String queueName) throws Exception {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
    
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    public default void consumeNoAck(Channel channel, String queueName) throws Exception {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
    
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }


    default void consumeCompeting(Channel channel, Delivery delivery) {
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
}
