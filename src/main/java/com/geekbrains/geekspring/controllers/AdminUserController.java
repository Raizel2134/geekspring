package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin-users-page";
    }

    @RequestMapping(value = "/{id}")
    public String editUsers(Model model, @PathVariable Long id) {
        SystemUser systemUser = userService.findBySystemUserName(userService.getById(id).getUserName());
        model.addAttribute("systemUser", systemUser);
        return "admin-users-page-edit";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String removeUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/save-change", method = RequestMethod.GET)
    public String saveChangeUser(Model model, SystemUser systemUser) {
        userService.saveChangeUser(systemUser);
        SystemUser systemUserChange = userService.findBySystemUserName(systemUser.getUserName());
        model.addAttribute("status", "Пользователь изменен");
        model.addAttribute("systemUser", systemUserChange);
        return "redirect:/admin/users";
    }
}
