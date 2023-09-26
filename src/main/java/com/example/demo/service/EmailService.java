package com.example.demo.service;

import com.example.demo.Constants.Constants;
import com.example.demo.entity.Player;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(String destinationEmail, Player player) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(Constants.SENDING_EMAIL_ADRESS);
            message.setTo(destinationEmail);
            message.setSubject("Email Verification Link");
            message.setText("Click this link to confirm your email address and complete setup for your account."
                    + "\n\nVerification Link: " + "http://localhost:8080/EmailVerification/ActivateAccount?" + player.getActivationCode());
            javaMailSender.send(message);
            log.info("Verification email was send to " + player.getEmail());
        } catch (Exception e) {
            System.out.println("Error sending email at " + e);
        }
    }

    public boolean validEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}