package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);
    SystemUser findBySystemUserName(String username);
    boolean save(SystemUser systemUser);
    boolean saveChangeUser(SystemUser systemUser);
    User getById(Long id);
    List<User> findAll();

    void deleteById(Long id);
}
