package com.example.demo.views;

import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.LogInService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;



@Route("/login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private static final Logger logger = LogManager.getLogger(LoginView.class);

    @Autowired
  LogInService loginService;

    @Autowired
    PlayerRepository playerRepository;


  public LoginView() {

    TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");

    add(
        new H1("Log in to save your data, or click Quickstart"),
        username,
        password,
        new Button("Login to play against non-user", event -> {
            if(playerRepository.findPlayerByNameAndPassword(username.getValue(), password.getValue()) != null){
              loginService.setNameOfLoggedInUserOne(username.getValue());
            loginService.setPlayerOneLoggedIn(true);
            UI.getCurrent().navigate("/game");
          } else {
              logger.info("entered username: "+username.getValue() + "entered password: " + password.getValue());
              Notification.show("Wrong credentials");
          }
        }),

        new RouterLink("Play against other user", MultiplayerView.class),
        new RouterLink("Register Account", RegisterView.class),
        new RouterLink("Quickstart", GameView.class),
        new RouterLink("Leaderboard", LeaderboardView.class)
    );

    setJustifyContentMode(JustifyContentMode.CENTER);
    setAlignItems(Alignment.CENTER);
  }
}