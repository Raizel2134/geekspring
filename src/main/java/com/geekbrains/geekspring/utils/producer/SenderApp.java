package com.geekbrains.geekspring.utils.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class SenderApp {
    private final static String QUEUE_NAME = "shop_cart";

    public void senderMessage(String msg) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()){
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
             channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("Product: " + msg + " add to cart.");
        }
    }
}
