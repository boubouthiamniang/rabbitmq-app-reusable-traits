package com.messaging.rabbitmq.endpoint;

import com.messaging.rabbitmq.traits.ChannelTraits;
import com.messaging.rabbitmq.traits.ConnectionTraits;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class ReceiverSample implements ConnectionTraits, ChannelTraits {
    private static final String QUEUE_NAME = "basic_queue";

    // Method to start listening for messages
    public void startListening() {
        try {
            // Step 1: Create a connection to the RabbitMQ server
            Connection connection = createConnection("localhost", 5672, "guest", "guest");

            // Step 2: Create a channel from the connection
            Channel channel = createChannel(connection);

            // Step 3: Declare the queue
            declareQueue(channel, QUEUE_NAME);

            // Step 4: Consume messages from the
            //consumeMessages(channel, QUEUE_NAME);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Instantiate Receiver and start listening
        ReceiverSample receiver = new ReceiverSample();
        receiver.startListening();
    }
}
