package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Route("register")
@Slf4j
public class RegisterView extends Composite {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PlayerService playerService;

    @Override
    protected Component initContent() {

        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm Password");
        TextField email = new TextField("Email address");

        VerticalLayout layout = new VerticalLayout(
                new H2("Register"),
                username,
                password1,
                password2,
                email,
                new Button("Done", event -> {
                    try {
                        register(
                                username.getValue(),
                                password1.getValue(),
                                password2.getValue(),
                                email.getValue()
                        );
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return layout;
    }


    private void register(String username, String password1, String password2, String email) throws InterruptedException {

        // Validate username
        if (username.isEmpty() || username.contains(" ") || username.equalsIgnoreCase("admin")) {
            Notification.show("Invalid username");
            return;
        }

        if (!password1.equals(password2) || password1.length() < 5) {
            Notification.show("Passwords don't match or are too short (min. 5 characters)");
            return;
        }

        if (!emailService.validEmail(email)) {
            Notification.show("Please enter valid email");
            return;
        }


        // Check if username already exists in database and respond accordingly
        Player player = playerRepository.findPlayerByName(username);
        if (player == null || !player.isAccountActivated()) {
            Thread.sleep(1500);
            Notification.show("Please verify your registration mail");
            Player newPlayer = new Player(username, playerService.hashPassword(password1), email);
            emailService.sendMail(email, newPlayer);
            if (player == null) {
                playerRepository.save(newPlayer);
                log.info("User {} was saved", newPlayer.getName());
            } else {
                playerRepository.updateActivationCodeByName(newPlayer.getActivationCode(), newPlayer.getName());
                playerRepository.updateEmailByName(newPlayer.getEmail(), newPlayer.getName());
            }
        } else {
            Notification.show("Username is taken");
        }
    }
}
