package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.utils.ShoppingCart;
import com.geekbrains.geekspring.utils.consumer.ReceiverApp;
import com.geekbrains.geekspring.utils.producer.SenderApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class ShoppingCartService {
    private ProductService productService;
    private SenderApp senderApp;
    private ReceiverApp receiverApp;
    private ShoppingCart shoppingCart;

    @Autowired
    public void setReceiverApp(ReceiverApp receiverApp){ this.receiverApp = receiverApp; }

    @Autowired
    public void setShoppingCart(ShoppingCart shoppingCart){ this.shoppingCart = shoppingCart; }

    @Autowired
    public void setSenderApp(SenderApp senderApp) {
        this.senderApp = senderApp;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ShoppingCart getCurrentCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void resetCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public void addToCart(HttpSession session, Long productId) throws IOException, TimeoutException {
        Product product = productService.getProductById(productId);
        senderApp.senderMessage(product.getTitle());
        addToCart(session, product);
    }

    public void addToCart(HttpSession session, Product product) {
        ShoppingCart cart = getCurrentCart(session);
        cart.add(product);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId);
        removeFromCart(session, product);
    }

    public void removeFromCart(HttpSession session, Product product) {
        ShoppingCart cart = getCurrentCart(session);
        cart.remove(product);
    }

    public void setProductCount(HttpSession session, Long productId, Long quantity) {
        ShoppingCart cart = getCurrentCart(session);
        Product product = productService.getProductById(productId);
        cart.setQuantity(product, quantity);
    }

    public void setProductCount(HttpSession session, Product product, Long quantity) {
        ShoppingCart cart = getCurrentCart(session);
        cart.setQuantity(product, quantity);
    }

    public double getTotalCost(HttpSession session) {
        return getCurrentCart(session).getTotalCost();
    }
}
