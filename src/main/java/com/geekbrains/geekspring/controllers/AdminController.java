package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Student;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String adminHome(Principal principal, Model model) {
        User user = userService.findByUserName(principal.getName());
        String email = "unknown";
        if (user != null) {
            email = user.getEmail();
        }
        model.addAttribute("email", email);
        return "admin-panel";
    }

    @RequestMapping(path = "/edit-form", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "edit-form";
    }

    @RequestMapping(path = "/edit/{id}", method = RequestMethod.GET)
    public String editUserForm(Model model, @PathVariable(value = "id") Long userId) {
        Optional<User> user = userService.findById(userId);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String showAddForm(User user) {
        userService.update(user);
        return "redirect:/admin/edit-form";
    }
}
