package com.messaging.rabbitmq.traits;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.messaging.rabbitmq.utils.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.AMQP.BasicProperties;

public interface ProducerConfirmsTraits {

    //BNG - Improve reuse declareQueue variant parameter
    default void publishMessagesIndividually(Channel channel, String queueName, String message, BasicProperties props) throws Exception {
        
        long start = System.nanoTime();
        for (int i = 0; i < Constants.MESSAGE_COUNT; i++) {
            channel.basicPublish("", queueName, props, message.getBytes());
            channel.waitForConfirmsOrDie(Constants.PUBLISH_CONFIRM_TIMEOUT);
        }
        long end = System.nanoTime();
        System.out.format("Published %,d messages individually in %,d ms%n", Constants.MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
    }

    default void publishMessagesInBatch(Channel channel, String queueName, String message, BasicProperties props, int batchSize) throws Exception {
        int outstandingMessageCount = 0;

        long start = System.nanoTime();
        for (int i = 0; i < Constants.MESSAGE_COUNT; i++) {
            channel.basicPublish("", queueName, props, message.getBytes());
            outstandingMessageCount++;

            if (outstandingMessageCount == batchSize) {
                channel.waitForConfirmsOrDie(5_000);
                outstandingMessageCount = 0;
            }
        }

        if (outstandingMessageCount > 0) {
            channel.waitForConfirmsOrDie(5_000);
        }
        long end = System.nanoTime();
        System.out.format("Published %,d messages in batch in %,d ms%n", Constants.MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
    }

    //Background
    default void handlePublishConfirmsAsynchronously(Channel channel, String queueName, String message, BasicProperties props) throws Exception {

        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // Define callbacks for ACK/NACK responses
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if (multiple) {
                outstandingConfirms.headMap(sequenceNumber + 1).clear();
            } else {
                outstandingConfirms.remove(sequenceNumber);
            }
            System.out.println("Message acknowledged: " + sequenceNumber);
        };

        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String failedMessage = outstandingConfirms.get(sequenceNumber);
            System.err.println("Message NOT acknowledged: " + sequenceNumber + " - " + failedMessage);
            outstandingConfirms.remove(sequenceNumber);
        };

        // Set up the confirm listener
        channel.addConfirmListener(ackCallback, nackCallback);

        // Publish multiple messages asynchronously
        for (int i = 0; i < 10; i++) {
            String msgToSend = message + " " + i;  // Avoid shadowing the parameter
            long nextSeqNo = channel.getNextPublishSeqNo();
            outstandingConfirms.put(nextSeqNo, msgToSend);

            channel.basicPublish("", queueName, props, msgToSend.getBytes(StandardCharsets.UTF_8));
            System.out.println("Published message: " + msgToSend);
        }

        // Wait to ensure all messages are processed
        Thread.sleep(5000);
    }
}
