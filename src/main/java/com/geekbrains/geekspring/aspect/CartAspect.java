package com.geekbrains.geekspring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class CartAspect{
    Logger logger = Logger.getLogger(CartAspect.class.getName());

    @Before("execution(public void com.geekbrains.geekspring.services.ShoppingCartService.*(..))")
    public void beforeAnyAddProductToCart(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("Был вызван следующий метод - " + methodSignature.getMethod().getName());
    }

    @Before("execution(public * com.geekbrains.geekspring.utils.ShoppingCart.add(..))")//Не проваливается в метод
    public void beforeAdd() {
        //
    }
}
