package com.geekbrains.geekspring;

import com.geekbrains.geekspring.services.ShoppingCartService;
import org.junit.Before;
import org.junit.Test;

public class JUnitTest {
    private ShoppingCartService shoppingCartService;

    @Before
    public void init() {
        shoppingCartService = new ShoppingCartService();
    }

    @Test
    public void addToCart(){
        shoppingCartService.addToCartNoSession(1l);
    }

    @Test
    public void removeToCart(){
        shoppingCartService.removeCartNoSession(1l);
    }
}
