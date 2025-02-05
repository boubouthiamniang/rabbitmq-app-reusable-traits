package com.messaging.rabbitmq.endpoint;

import com.messaging.rabbitmq.traits.ChannelTraits;
import com.messaging.rabbitmq.traits.ConnectionTraits;
import com.messaging.rabbitmq.traits.MessageTraits;
import com.messaging.rabbitmq.traits.ProducerTraits;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SenderSample implements ConnectionTraits, ChannelTraits, ProducerTraits, MessageTraits {
    private static final String QUEUE_NAME = "hello";

    public void sendMessage() {
        try {
            // Step 1: Create a connection to the RabbitMQ server
            Connection connection = createConnection("localhost", 5672, "guest", "guest");

            // Step 2: Create a channel from the connection
            Channel channel = createChannel(connection);

            // Step 3: Declare the queue
            declareQueue(channel, QUEUE_NAME);

            // Step 4: Prepare the message
            String message = prepareTextMessage();

            // Step 5: Publish the message to the queue
            publish(channel, "", QUEUE_NAME, message);

            System.out.println(" [x] Sent '" + message + "'");

            // Closing the channel and connection
            channel.close();
            connection.close();

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Instantiate Sender and send a message
        SenderSample sender = new SenderSample();
        sender.sendMessage();
    }
}
