package com.messaging.rabbitmq.traits;

public class ExchangeTraits {
    
    default void declareExchange(String exchangeName, String exchangeType) throws Exception {
        channel.exchangeDeclare(exchangeName, exchangeType);
    }
}
