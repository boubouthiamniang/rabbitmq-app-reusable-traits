package com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.AMQP;

public interface RPCServerTraits {

    // Handle incoming request
    public default void handleMessage(Channel channel, String queueName) throws Exception {
        // Create a callback to process incoming requests
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String response = "";
            try {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [.] Processing " + message);
                response = processRequest(message);
            } catch (RuntimeException e) {
                System.out.println(" [.] Error: " + e.toString());
            } finally {
                // Send the response back to the client
                String replyTo = delivery.getProperties().getReplyTo();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();
                channel.basicPublish("", replyTo, replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        // Set up the consumer
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }

    // A simple function to simulate processing
    public default String processRequest(String message) {
        return "Processed: " + message;
    }
}
