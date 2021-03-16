package com.geekbrains.geekspring.repositories;

import com.geekbrains.geekspring.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
