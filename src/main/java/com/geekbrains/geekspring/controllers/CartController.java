package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.services.ShoppingCartService;
import com.geekbrains.geekspring.utils.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ShoppingCartService shoppingCartService;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public String cartPage(Model model, HttpSession httpSession) {
        ShoppingCart cart = shoppingCartService.getCurrentCart(httpSession);
        model.addAttribute("cart", cart);
        return "cart-page";
    }

    @GetMapping(value = "/remove/{id}")
    public String removeCart(@PathVariable("id") Long id, HttpSession httpSession){
        shoppingCartService.removeFromCart(httpSession, id);
        return "redirect:/cart";
    }
}
