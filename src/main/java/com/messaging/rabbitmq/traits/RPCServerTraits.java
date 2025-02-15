package com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

public interface RPCServerTraits {

    // Handle incoming request
    public default void handleMessage(Channel channel, String queueName, String correlationId, byte[] body) throws Exception {
        String response = "";
        try {
            String message = new String(body, "UTF-8");

            // Process the message (this should be a custom method)
            response = processRequest(message);

        } catch (Exception e) {
            System.out.println(" [.] Error: " + e.getMessage());
            response = "Error processing message";
        } finally {
            // Send the response back to the reply queue with correlation ID
            BasicProperties replyProps = new BasicProperties.Builder()
                    .correlationId(correlationId) // Maintain correlation ID for matching the response
                    .build();

            // Publish the response to the reply queue
            channel.basicPublish("", queueName, replyProps, response.getBytes("UTF-8"));
        }
    }

    // A simple function to simulate processing
    public default String processRequest(String message) {
        return "Processed: " + message;
    }
}
