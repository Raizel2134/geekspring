package com.geekbrains.geekspring.interfaces;

import com.geekbrains.geekspring.entities.Greeting;

public interface IShopControllerWs {
    void sendMessage(String destination, Greeting message);
}
