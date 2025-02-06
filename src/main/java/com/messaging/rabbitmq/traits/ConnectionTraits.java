package com.messaging.rabbitmq.traits;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public interface ConnectionTraits {
    default public Connection createConnection(String host, int port, String username, String password) throws Exception {
        try(ConnectionFactory factory = new ConnectionFactory()) {
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            return factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default public Connection createConnectionWithPortAndCredential(String host, int port, String username, String password) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory.newConnection();
    }

    default public void closeConnection(Connection connection) throws IOException {
        connection.close();
    }
}
