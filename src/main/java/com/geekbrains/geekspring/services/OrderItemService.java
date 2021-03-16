package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.OrderItem;
import com.geekbrains.geekspring.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService{
    OrderItemRepository orderItemRepository;

    @Autowired
    private void setOrderItemRepository(OrderItemRepository orderItemRepository){
        this.orderItemRepository = orderItemRepository;
    }

    public void addOrderItems(List<OrderItem> orderItems){
        for (OrderItem item: orderItems) {
            orderItemRepository.save(item);
        }
    }
}
