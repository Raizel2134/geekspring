package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.DeliveryAddress;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.repositories.DeliveryAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryAddressService {
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    public void setDeliveryAddressRepository(DeliveryAddressRepository deliveryAddressRepository) {
        this.deliveryAddressRepository = deliveryAddressRepository;
    }

    public List<DeliveryAddress> getUserAddresses(Long userId) {
        return deliveryAddressRepository.findAllByUserId(userId);
    }

    public DeliveryAddress getAddresses(Long id, User user) {
        return deliveryAddressRepository.findByIdAndUser(id, user);
    }

    public DeliveryAddress setUserAddresses(User user, String address){
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setUser(user);
        deliveryAddress.setAddress(address);
        return deliveryAddressRepository.save(deliveryAddress);
    }
}
