package com.messaging.rabbitmq.traits;

public interface MessageProcessor {
    // Functional interface for processing messages
    @FunctionalInterface
    public interface MessageProcessor {
        String processMessage(String message) throws Exception;
    }
}
