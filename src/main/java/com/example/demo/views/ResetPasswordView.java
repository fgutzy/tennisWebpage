package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/resetPassword")
@Slf4j
public class ResetPasswordView extends VerticalLayout {

    @Autowired
    PlayerRepository playerRepository;

    PlayerService playerService;

    @Autowired
    public ResetPasswordView(PlayerService playerService) {
        this.playerService = playerService;

        PasswordField password = new PasswordField("New Password");
        PasswordField repeatPassword = new PasswordField("Repeat New Password");

        add(
                password,
                repeatPassword,
                new Button("Set new password", event -> {

                    String playerName = VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") != null ?
                            (String) VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") : null;

                    Player player = playerRepository.findPlayerByName(playerName);

                    if (player != null && passwordsMatch(password.getValue(), repeatPassword.getValue())) {
                        String hashedPassword = playerService.hashPassword(password.getValue());
                        playerRepository.updatePasswordByName(hashedPassword, playerName);
                        Notification.show("Password was changed.");
                        UI.getCurrent().navigate("/");
                        log.info("Password of user {} was changed", playerName);
                    } else {
                        Notification.show("Not logged in or passwords are not valid (dont match or too short).");
                    }
                })
        );
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

    }

    private boolean passwordsMatch(String password1, String password2) {
        return password1.equals(password2) && !password1.isEmpty();
    }
}
