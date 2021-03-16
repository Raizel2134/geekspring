package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-orders-page";
    }

    @RequestMapping(value = "/ready/{id}", method = RequestMethod.GET)
    public void orderReady(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) throws Exception {
        Order order = orderService.findById(id);
        orderService.changeOrderStatus(order, 2L);
        response.sendRedirect(request.getHeader("referer"));
    }
}
