package com.messaging.rabbitmq.traits;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public interface RPCCLientTraits {

    default String call(Channel channel, String message, String requestQueueName, String replyQueueName) throws IOException, InterruptedException, ExecutionException {
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final CompletableFuture<String> response = new CompletableFuture<>();

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {});

        String result = response.get();
        channel.basicCancel(ctag);
        return result;
    }
}
