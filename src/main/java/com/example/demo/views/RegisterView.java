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
        setAlignItems(FlexComponent.Alignment.CENTER);    }


    private void register(TextField username, PasswordField password1, PasswordField password2, TextField email) throws InterruptedException {
        String usernameEntered = username.getValue();
        String password1Entered = password1.getValue();
        String password2Entered = password2.getValue();
        String emailEntered = email.getValue();

        System.out.println(usernameEntered + " username");
        System.out.println(password1Entered + " password1");
        System.out.println(password2Entered+ " password2");
        System.out.println(emailEntered + " email");



        // Validate username
        if (usernameEntered.isEmpty() || usernameEntered.contains(" ") || usernameEntered.equalsIgnoreCase("admin")) {
            Notification.show("Invalid username");
            return;
        }

        if (!password1Entered.equals(password2Entered) || password1Entered.length() < 5) {
            Notification.show("Passwords don't match or are too short (min. 5 characters)");
            return;
        }

        if (!emailService.validEmail(emailEntered)) {
            Notification.show("Please enter valid email");
            return;
        }


        // Check if username already exists in database and respond accordingly
        Player player = playerRepository.findPlayerByName(usernameEntered);
        if (player == null || !player.isAccountActivated() && playerRepository.findPlayerByEmail(emailEntered) == null) {
            Notification.show("Please verify your registration mail");
            username.clear();
            password1.clear();
            password2.clear();
            email.clear();
            Player newPlayer = new Player(usernameEntered, playerService.hashPassword(password1Entered), emailEntered);
            emailService.sendActivationEmail(emailEntered, newPlayer);
            if (player == null) {
                playerRepository.save(newPlayer);
                log.info("User {} was saved", newPlayer.getName());
            } else {
                playerRepository.updateActivationCodeByName(newPlayer.getActivationCode(), newPlayer.getName());
                playerRepository.updateEmailByName(newPlayer.getEmail(), newPlayer.getName());
            }
        } else {
            Notification.show("Username or email is taken");
        }
    }
}
