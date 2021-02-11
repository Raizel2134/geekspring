package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Greeting;
import com.geekbrains.geekspring.entities.Message;
import com.geekbrains.geekspring.interfaces.IShopControllerWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ShopControllerWs implements IShopControllerWs {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(3000);
        return new Greeting(message.getName());
    }

    public void sendMessage(String destination, Greeting message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }
}
