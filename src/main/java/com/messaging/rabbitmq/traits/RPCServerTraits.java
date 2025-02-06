package com.messaging.rabbitmq.traits;

import com.messaging.rabbitmq.traits.MessageProcessor.MessageProcessor;
import com.rabbitmq.client.AMQP;

public interface RPCServerTraits {

    default void handleMessage(Channel channel, String queueName, String correlationId, byte[] body, MessageProcessor processor) throws Exception {
        String response = "";
        try {
            String message = new String(body, "UTF-8");

            // Use the custom processor to handle the message
            response = processor.processMessage(message);

        } catch (Exception e) {
            System.out.println(" [.] Error: " + e.getMessage());
        } finally {
            // Send the response back to the reply queue (if any)
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlationId) // Maintain the correlation ID for response matching
                    .build();

            // Publish the response to the reply queue
            channel.basicPublish("", queueName, replyProps, response.getBytes("UTF-8"));
        }
    }
}
