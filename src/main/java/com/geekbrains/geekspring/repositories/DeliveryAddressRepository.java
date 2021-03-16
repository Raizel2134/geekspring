package com.geekbrains.geekspring.repositories;

import com.geekbrains.geekspring.entities.DeliveryAddress;
import com.geekbrains.geekspring.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findAllByUserId(Long userId);

    DeliveryAddress findByIdAndUser(Long id, User user);
}
