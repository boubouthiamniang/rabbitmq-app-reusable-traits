package com.messaging.rabbitmq.traits;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public interface ChannelTraits {

    default public Channel createChannel(Connection connection) throws Exception {
        return connection.createChannel();
    }  
}
