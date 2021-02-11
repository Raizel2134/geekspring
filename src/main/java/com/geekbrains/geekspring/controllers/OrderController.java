package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.DeliveryAddress;
import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.DeliveryAddressService;
import com.geekbrains.geekspring.services.OrderService;
import com.geekbrains.geekspring.services.UserService;
import com.geekbrains.geekspring.utils.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private DeliveryAddressService deliverAddressService;
    private ShoppingCart shoppingCart;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDeliverAddressService(DeliveryAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    @Autowired
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @GetMapping("/order/fill")
    public String orderFill(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        model.addAttribute("cart", shoppingCart);
        model.addAttribute("deliveryAddresses", deliverAddressService.getUserAddresses(user.getId()));
        return "order-filler";
    }

    @PostMapping("/order/confirm")
    public String orderConfirm(Model model, Principal principal, @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("deliveryAddress") Long deliveryAddressId) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCart, user);
        order.setDeliveryAddress((DeliveryAddress) deliverAddressService.getUserAddresses(deliveryAddressId));
        order.setPhoneNumber(phoneNumber);
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(0.0);
        order = orderService.saveOrder(order);
        model.addAttribute("order", order);
        return "order-before-purchase";
    }

    @GetMapping("/order/result/{id}")
    public String orderConfirm(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order confirmedOrder = orderService.findById(id);
        if (!user.getId().equals(confirmedOrder.getUser().getId())) {
            return "redirect:/";
        }
        model.addAttribute("order", confirmedOrder);
        return "order-result";
    }
}
