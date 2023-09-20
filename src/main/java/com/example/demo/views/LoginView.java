package com.example.demo.views;

import com.example.demo.repository.PlayerRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;

@Route("/login")
@PageTitle("login")
public class LoginView extends VerticalLayout {


    @Autowired
    PlayerRepository playerRepository;
    public LoginView() {

        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");

        add(
                username,
                password,
                new Button("Login", event -> {
                    if (playerRepository.findPlayerByNameAndPassword(username.getValue(), password.getValue()) != null) {
                        VaadinSession.getCurrent().setAttribute("username", username.getValue());
                        VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserOne", username.getValue());
                        VaadinSession.getCurrent().setAttribute("playerOneLoggedIn", true);
                        UI.getCurrent().navigate("/game");
                    } else {
                        Notification.show("Wrong credentials");
                    }
                }),
                new RouterLink("Play against other user", MultiplayerView.class),
                new RouterLink("Register Account", RegisterView.class)
        );
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

    }
}
