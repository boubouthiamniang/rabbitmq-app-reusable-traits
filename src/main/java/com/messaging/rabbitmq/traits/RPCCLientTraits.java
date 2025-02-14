package com.messaging.rabbitmq.traits;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.AMQP.BasicProperties;

public interface RPCCLientTraits {

    default void publishRPCRequest(Channel channel, String message, String exchangeName, String requestQueueName, String replyQueueName, boolean isAutoAck, BasicProperties props, String correlationId) throws IOException, InterruptedException, ExecutionException {
        channel.basicPublish(exchangeName, requestQueueName, props, message.getBytes(StandardCharsets.UTF_8));

        // Use a BlockingQueue to wait for the response
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId() != null &&
                delivery.getProperties().getCorrelationId().equals(correlationId)) {
                response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        };

        // Consume messages from the reply queue
        channel.basicConsume(requestQueueName, isAutoAck, deliverCallback, consumerTag -> { });
    }
}
