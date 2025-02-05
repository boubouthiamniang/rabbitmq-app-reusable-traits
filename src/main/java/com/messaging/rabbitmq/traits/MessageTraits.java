package com.messaging.rabbitmq.traits;

public interface MessageTraits {

    default String prepareTextMessage() {
        return "I am the default message";
    }
}
