package com.example.demo.service;

import com.example.demo.Constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.resource.spi.AuthenticationMechanism;
@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;
    public void sendMail() {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constants.SENDING_EMAIL_ADRESS);
        message.setTo("ferdinand.gutzy@gmx.de");
        message.setSubject("Email Verification Link");
        message.setText("Click this link to confirm your email address and complete setup for your account."
                + "\n\nVerification Link: " + "http://localhost:8080/EmailVerification/ActivateAccount?key1=" +
                "ferdinand.gutzy@gmx.de" + "&key2=" + "t1estS4ring");

        javaMailSender.send(message);
    }

}