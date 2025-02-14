package com.messaging.rabbitmq.utils;

import java.util.HashMap;
import java.util.Map;

public class QueueArgumentsBuilder {
    private final Map<String, Object> arguments = new HashMap<>();

    public QueueArgumentsBuilder setType(String type) {
        arguments.put("x-queue-type", type);
        return this;
    }

    public QueueArgumentsBuilder setTimeToLive(int timeToLive) {
        arguments.put("x-message-ttl", timeToLive);
        return this;
    }

    public QueueArgumentsBuilder setLengthLimit(int lengthLimit) {
        arguments.put("x-max-length", lengthLimit);
        return this;
    }

    public QueueArgumentsBuilder setRedeliveryLimit(int redeliveryLimit) {
        arguments.put("x-delivery-limit", redeliveryLimit);
        return this;
    }

    public QueueArgumentsBuilder setMaxNbPriorities(int maxNbPriorities) {
        arguments.put("x-max-priority", maxNbPriorities);
        return this;
    }

    public Map<String, Object> build() {
        return arguments;
    }
}
