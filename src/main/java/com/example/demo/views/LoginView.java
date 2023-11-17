package com.example.demo.views;

import com.example.demo.persitence.repository.PlayerRepository;
import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDataDto;
import com.example.demo.service.dto.PlayerDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/login")
@PageTitle("login")
@Slf4j
public class LoginView extends VerticalLayout {


    @Autowired
    PlayerRepository playerRepository;

    PlayerService playerService;

    @Autowired
    public LoginView(PlayerService playerService) {
        this.playerService = playerService;

        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");

        add(
                username,
                password,
                new Button("Login", event -> {
                    PlayerDataDto playerDto = playerService.findPlayerByNameAndPassword(username.getValue(), password.getValue());
                    if (playerDto != null && playerDto.isAccountActivated()) {
                            VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserOne", username.getValue());
                            VaadinSession.getCurrent().setAttribute("uuid", playerDto.getId());
                            log.info("User {} logged in", username);
                            UI.getCurrent().navigate("/game");
                        } else {
                        Notification.show("User doesnt exist, or wrong credentials");
                        log.warn("User tried to log in with wrong credentials.");
                    }

                }),
                new RouterLink("Reset password", ForgotPasswordView.class),
                new RouterLink("Play against other user", MultiplayerView.class),
                new RouterLink("Register Account", RegisterView.class)
        );
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

    }
}
