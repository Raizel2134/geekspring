package com.geekbrains.geekspring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherController {
    @GetMapping("/index")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/contact")
    public String showContact() {
        return "contact";
    }

    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }
}
