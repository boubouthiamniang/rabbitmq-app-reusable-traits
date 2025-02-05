package com.messaging.rabbitmq.traits;

public interface Payload {

    default String prepareTextMessage() {
        return "I am the default message";
    }
}
