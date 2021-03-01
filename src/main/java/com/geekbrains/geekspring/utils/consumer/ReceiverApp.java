package com.geekbrains.geekspring.utils.consumer;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class ReceiverApp {
    private final static String QUEUE_NAME = "shop_cart";
    private Channel channel = null;

    public void receivingMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Wait message");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received messages: " + msg);
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
