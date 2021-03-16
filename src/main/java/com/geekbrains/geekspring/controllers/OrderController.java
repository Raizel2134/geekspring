package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.DeliveryAddress;
import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.*;
import com.geekbrains.geekspring.utils.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private DeliveryAddressService deliverAddressService;
    private ShoppingCart shoppingCart;
    private OrderStatusService orderStatusService;
    private ShoppingCartService shoppingCartService;
    private MailService mailService;
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    public void setDeliveryAddressService(DeliveryAddressService deliveryAddressService) {
        this.deliveryAddressService = deliveryAddressService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJavaMailSender(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
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

    @RequestMapping
    public String orderFill(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        model.addAttribute("cart", shoppingCart);
        model.addAttribute("deliveryAddresses", deliverAddressService.getUserAddresses(user.getId()));
        return "place-order";
    }

    @RequestMapping(value = "/fill", method = RequestMethod.GET)
    public String orderFill(Model model, HttpServletRequest httpServletRequest, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        List<DeliveryAddress> deliveryAddresses = deliveryAddressService.getUserAddresses(user.getId());
        model.addAttribute("order", order);
        model.addAttribute("deliveryAddresses", deliveryAddresses);
        return "place-order";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public String orderConfirm(Model model, HttpServletRequest httpServletRequest, Principal principal, @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("deliveryAddress") Long deliveryAddressId) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        ShoppingCart cart = shoppingCartService.getCurrentCart(httpServletRequest.getSession());
        order.setStatus(orderStatusService.getStatusById(1l));
        order.setDeliveryAddress(deliverAddressService.getAddresses(deliveryAddressId, user));
        order.setPhoneNumber(phoneNumber);
        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(100.0);
        Order orderNew = orderService.saveOrder(order);
        model.addAttribute("order", orderNew);
        model.addAttribute("user", user);
        model.addAttribute("carts", cart);
        return "order";
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/send-mail/{idOrder}", method = RequestMethod.GET)
    public String sendMail(@PathVariable Long idOrder, Principal principal) throws MessagingException {
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.findById(idOrder);
        mailService.sendEmailWithAttachment(user, order);
        return "redirect:/index";
    }
}
