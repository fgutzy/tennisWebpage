package com.example.demo.views;

import com.example.demo.service.EmailService;
import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDataDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/forgotPassword")
@PageTitle("Forgot Password")
@Slf4j
public class ForgotPasswordView extends VerticalLayout {

    @Autowired
    PlayerService playerService;

    EmailService emailService;
    @Autowired
    public ForgotPasswordView(EmailService emailService) {
        this.emailService = emailService;

        TextField email = new TextField ("Enter email");

        add(
                email,
                new Button("Send", event -> {
                    Notification.show("Check your mails");
                    //only send mail if player was found
                    PlayerDataDto player = playerService.findPlayerByEmail(email.getValue());
                    //PlayerDto player = playerService.findPlayerByEmail(email.getValue());
                    if (player != null) {
                        emailService.sendPasswordResetEmail(email.getValue(), player);
                    }
                    email.clear();
                })
        );

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }
}

