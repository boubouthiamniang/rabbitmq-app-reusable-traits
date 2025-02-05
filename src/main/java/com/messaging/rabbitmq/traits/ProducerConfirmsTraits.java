package com.messaging.rabbitmq.traits;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.messaging.rabbitmq.utils.TimeoutManagement;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

public class ProducerConfirmsTraits {

    //BNG - Improve reuse declareQueue variant parameter
    default publishMessagesIndividually(Channel channel, String queueName) throws Exception {
        static final int MESSAGE_COUNT = 50000;

        ch.queueDeclare(queueName, false, false, true, null);

        ch.confirmSelect();
        long start = System.nanoTime();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String body = String.valueOf(i);
            ch.basicPublish("", queueName, null, body.getBytes());
            ch.waitForConfirmsOrDie(5000);
        }
        long end = System.nanoTime();
        System.out.format("Published %,d messages individually in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
    }

    default publishMessagesInBatch(Channel channel, String queueName) throws Exception {

        static final int MESSAGE_COUNT = 50000;

        ch.queueDeclare(queueName, false, false, true, null);
        ch.confirmSelect();

        int batchSize = 100;
        int outstandingMessageCount = 0;

        long start = System.nanoTime();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String body = String.valueOf(i);
            ch.basicPublish("", queueName, null, body.getBytes());
            outstandingMessageCount++;

            if (outstandingMessageCount == batchSize) {
                ch.waitForConfirmsOrDie(5_000);
                outstandingMessageCount = 0;
            }
        }

        if (outstandingMessageCount > 0) {
            ch.waitForConfirmsOrDie(5_000);
        }
        long end = System.nanoTime();
        System.out.format("Published %,d messages in batch in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
    }

    //Background
    static void handlePublishConfirmsAsynchronously(Channel channel, String queueName) throws Exception {
        ch.queueDeclare(queueName, false, false, true, null);

        ch.confirmSelect();

        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                        sequenceNumber, true
                );
                confirmed.clear();
            } else {
                outstandingConfirms.remove(sequenceNumber);
            }
        };

        ch.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
            String body = outstandingConfirms.get(sequenceNumber);
            System.err.format(
                    "Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n",
                    body, sequenceNumber, multiple
            );
            cleanOutstandingConfirms.handle(sequenceNumber, multiple);
        });

        long start = System.nanoTime();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String body = String.valueOf(i);
            outstandingConfirms.put(ch.getNextPublishSeqNo(), body);
            ch.basicPublish("", queue, null, body.getBytes());
        }

        if (!TimeoutManagement.waitUntil(Duration.ofSeconds(60), () -> outstandingConfirms.isEmpty())) {
            throw new IllegalStateException("All messages could not be confirmed in 60 seconds");
        }
        long end = System.nanoTime();
        System.out.format("Published %,d messages and handled confirms asynchronously in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
    } 
}
