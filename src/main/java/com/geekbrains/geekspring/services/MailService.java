package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {
    private JavaMailSender javaMailSender;

    @Autowired
    private void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }



    public void sendEmailWithAttachment(User user, Order order) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());

        helper.setSubject("Информация о заказе");
        helper.setText("<h1>Информация о вашем заказе</h1>", true);

        helper.setText(generateMail(user, order), true);

        javaMailSender.send(msg);
    }

    private String generateMail(User user, Order order) {
        StringBuilder stringBuilder = new StringBuilder();
        String stringOutput = "null";

        stringBuilder.append("<strong>Name: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(user.getFirstName() + user.getLastName());
        stringBuilder.append("<br>");

        stringBuilder.append("<strong>Email: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(user.getEmail());
        stringBuilder.append("<br>");

        stringBuilder.append("<strong>Delivery address: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(order.getDeliveryAddress().getAddress());
        stringBuilder.append("<br>");

        stringBuilder.append("<strong>Phone number: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(user.getPhone());
        stringBuilder.append("<br>");

        stringBuilder.append("<strong>Order number: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(order.getId());
        stringBuilder.append("<br>");

        stringBuilder.append("<strong>Date order: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(order.getCreateAt());
        stringBuilder.append("<br>");

        stringBuilder.append("<strong>Date delivery: ");
        stringBuilder.append("</strong>");
        stringBuilder.append(order.getDeliveryDate());
        stringBuilder.append("<br>");

        try {
            stringOutput = new String(stringBuilder.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return stringOutput;
    }
}
