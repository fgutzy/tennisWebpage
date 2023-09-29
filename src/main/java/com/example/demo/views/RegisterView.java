package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.GeneratedVaadinTextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.sign.PositiveOrZeroValidatorForLong;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Email;


@Route("register")
@Slf4j
public class RegisterView extends VerticalLayout {

    @Autowired
    PlayerRepository playerRepository;

    EmailService emailService;

    PlayerService playerService;


    @Autowired
    public RegisterView(EmailService emailService, PlayerService playerService) {
        this.emailService = emailService;
        this.playerService = playerService;

        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm Password");
        TextField email = new TextField("Email address");


        add(
                new H2("Register"),
                username,
                password1,
                password2,
                email,
                new Button("Done", event -> {
                    try {
                        register(
                                username,
                                password1,
                                password2,
                                email
                        );
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }


    private void register(TextField username, PasswordField password1, PasswordField password2, TextField email) throws InterruptedException {

        // Validate username
        if (username.getValue().isEmpty() || username.getValue().contains(" ") || username.getValue().equalsIgnoreCase("admin")) {
            Notification.show("Invalid username");
            return;
        }

        if (!password1.getValue().equals(password2.getValue()) || password1.getValue().length() < 5) {
            Notification.show("Passwords don't match or are too short (min. 5 characters)");
            return;
        }

        if (!emailService.validEmail(email.getValue())) {
            Notification.show("Please enter valid email");
            return;
        }

        //check if Player is allowed to register and act accordingly
        Player existingPlayerByEmail = playerRepository.findPlayerByEmail(email.getValue());

        if (existingPlayerByEmail != null) {
            Notification.show("You already created an account with this email");
        } else {
            Player existingPlayerByUsername = playerRepository.findPlayerByName(username.getValue());
            if (existingPlayerByUsername == null) { // Both email and username are available, create a new player
                Player newPlayer = new Player(username.getValue(), playerService.hashPassword(password1.getValue()), email.getValue());
                playerRepository.save(newPlayer);
                notifyAndClearFields(email.getValue(), newPlayer, username, password1, password2, email);
            } else if (!existingPlayerByUsername.isAccountActivated()) { // Username is taken, but the account is not activated, update activation code and email
                Player updatedPlayer = new Player(username.getValue(), password1.getValue(), email.getValue());
                playerRepository.updateActivationCodeByName(updatedPlayer.getActivationCode(), updatedPlayer.getName());
                playerRepository.updateEmailByName(email.getValue(), updatedPlayer.getName());
                notifyAndClearFields(email.getValue(), updatedPlayer, username, password1, password2, email);
            } else {// Username is taken and the account is activated
                Notification.show("Username taken");
            }
        }
    }


    private void notifyAndClearFields(String destinationEmail, Player registeredPlayer, TextField username, PasswordField password1,PasswordField password2, TextField email ){
        emailService.sendActivationEmail(destinationEmail, registeredPlayer);
        log.info("User {} was saved", registeredPlayer.getName());
        Notification.show("Please verify your registration email");
        username.clear();
        password1.clear();
        password2.clear();
        email.clear();
    }
}

