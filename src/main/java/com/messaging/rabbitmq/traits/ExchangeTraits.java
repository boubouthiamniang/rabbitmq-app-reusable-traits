package com.messaging.rabbitmq.traits;

import com.rabbitmq.client.Channel;

public interface ExchangeTraits {

    default void declareExchange(Channel channel, String exchangeName, String exchangeType) throws Exception {
        channel.exchangeDeclare(exchangeName, exchangeType);
    }
}
