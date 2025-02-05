package com.messaging.rabbitmq.traits;

import java.util.Map;

public interface MessageTraits {

    default String prepareTextMessage() {
        return "I am the default message";
    }

    public static BasicProperties createMessageProperties(
        String contentType,
        String contentEncoding,
        Map<String, Object> headers,
        Integer deliveryMode,
        Integer priority,
        String correlationId,
        String replyTo,
        String expiration,
        String messageId,
        Date timestamp,
        String type,
        String userId,
        String appId,
        String clusterId) {
        
        return new BasicProperties.Builder()
            .contentType(contentType != null ? contentType : "text/plain")
            .contentEncoding(contentEncoding)
            .headers(headers)
            .deliveryMode(deliveryMode != null ? deliveryMode : 1)
            .priority(priority != null ? priority : 0)
            .correlationId(correlationId)
            .replyTo(replyTo)
            .expiration(expiration)
            .messageId(messageId)
            .timestamp(timestamp)
            .type(type)
            .userId(userId)
            .appId(appId)
            .clusterId(clusterId)
            .build();
    }
}
