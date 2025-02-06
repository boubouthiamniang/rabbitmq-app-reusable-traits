package com.messaging.rabbitmq.endpoint-samples;

public class RPCClientSample {
    
    public static void main(String[] argv) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            String requestQueueName = "rpc_queue";

            for (int i = 0; i < 32; i++) {
                String message = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + message + ")");

                final String corrId = UUID.randomUUID().toString();
                String replyQueueName = channel.queueDeclare().getQueue();
                AMQP.BasicProperties props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(corrId)
                        .replyTo(replyQueueName)
                        .build();

                channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
                
                final CompletableFuture<String> response = new CompletableFuture<>();
                String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                    if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                        response.complete(new String(delivery.getBody(), "UTF-8"));
                    }
                }, consumerTag -> {});

                String result = response.get();
                channel.basicCancel(ctag);
                System.out.println(" [.] Got '" + result + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
