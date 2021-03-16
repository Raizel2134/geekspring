package com.geekbrains.geekspring.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "Какая-то ошибка!";
        String errorCode = "";
        if (status != null) {
            errorCode = status.toString();
        } else {
            errorCode = "???";
            errorPage.addObject("errorMsg", errorMsg);
            errorPage.addObject("errorCode", errorCode);
            return errorPage;
        }

        switch (status.toString()) {
            case "400": {
                errorMsg = "Неверный запрос!";
                errorCode = "400";
                break;
            }
            case "401": {
                errorMsg = "Unauthorized!";
                errorCode = "401";
                break;
            }
            case "404": {
                errorMsg = "Страница не найдена!";
                errorCode = "404";
                break;
            }
            case "403": {
                errorMsg = "Нет доступа!";
                errorCode = "403";
                break;
            }
            case "405": {
                errorMsg = "Method Not Allowed!";
                errorCode = "405";
                break;
            }
            case "500": {
                errorMsg = "Внутренняя ошибка сервера!";
                errorCode = "500";
                break;
            }
        }
        errorPage.addObject("errorMsg", errorMsg);
        errorPage.addObject("errorCode", errorCode);
        return errorPage;

    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
