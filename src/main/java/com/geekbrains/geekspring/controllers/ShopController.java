package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.DeliveryAddress;
import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.*;
import com.geekbrains.geekspring.utils.ProductFilter;
import com.geekbrains.geekspring.utils.consumer.ReceiverApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private MailService mailService;
    private UserService userService;
    private OrderService orderService;
    private ProductService productService;
    private ShoppingCartService shoppingCartService;
    private DeliveryAddressService deliverAddressService;
    private ReceiverApp receiverApp;

    @Autowired
    public void setReceiverApp(ReceiverApp receiverApp){ this.receiverApp = receiverApp; }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

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
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam Map<String, String> requestParams,
                           @RequestParam(value = "page") Optional<Integer> page
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        ProductFilter productFilter = new ProductFilter(requestParams);

        Page<Product> products = productService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, productFilter.getSpec());

        model.addAttribute("products", products.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());

        System.out.println(productFilter.toString());

        model.addAttribute("filters", productFilter.toString());
        return "shop-page";
    }

    @GetMapping("/product_info/{id}")
    public String productPage(Model model, @PathVariable(value = "id") Long id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product-page";
    }

    @GetMapping(value = "/cart/add/{id}")
    public String addProductToCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest)
            throws IOException, TimeoutException {
        shoppingCartService.addToCart(httpServletRequest.getSession(), id);
        String referrer = httpServletRequest.getHeader("referer");
        return "redirect:" + referrer;
    }

    @GetMapping("/order/fill")
    public String orderFill(Model model, HttpServletRequest httpServletRequest, Principal principal)
            throws IOException, TimeoutException {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        List<DeliveryAddress> deliveryAddresses = deliverAddressService.getUserAddresses(user.getId());
        model.addAttribute("order", order);
        model.addAttribute("deliveryAddresses", deliveryAddresses);
        receiverApp.receivingMessage();
        return "order-filler";
    }

    @PostMapping("/order/confirm")
    public String orderConfirm(Model model, HttpServletRequest httpServletRequest, @ModelAttribute(name = "order") Order orderFromFrontend, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        order.setDeliveryAddress(orderFromFrontend.getDeliveryAddress());
        order.setPhoneNumber(orderFromFrontend.getPhoneNumber());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(0.0);
        order = orderService.saveOrder(order);
        model.addAttribute("order", order);
        return "order-filler";
    }
}
