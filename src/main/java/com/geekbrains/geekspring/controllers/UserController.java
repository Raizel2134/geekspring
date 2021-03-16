package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.DeliveryAddressService;
import com.geekbrains.geekspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class UserController {
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam Map<String, String> requestParams) {
        return "login";
    }

    @PostMapping("/authenticateTheUser")
    public String successLogin() {
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/index";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("systemUser", new SystemUser());
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistrationForm(@Valid @ModelAttribute("systemUser") SystemUser theSystemUser, BindingResult theBindingResult, Model theModel) {
        String userName = theSystemUser.getUserName();
        if (theBindingResult.hasErrors()) {
            return "registration";
        }
        User existing = userService.findByUserName(userName);
        if (existing != null) {
            theModel.addAttribute("systemUser", theSystemUser);
            theModel.addAttribute("registrationError", "Это имя пользователя уже существует");
            return "registration";
        }
        userService.save(theSystemUser);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SystemUser systemUser = userService.findBySystemUserName(userDetails.getUsername());
        model.addAttribute("systemUser", systemUser);
        return "user";
    }

    @PostMapping("/editUser")
    public String editUserInfo(SystemUser systemUser) {
        userService.saveChangeUser(systemUser);
        return "redirect:/user";
    }
}
