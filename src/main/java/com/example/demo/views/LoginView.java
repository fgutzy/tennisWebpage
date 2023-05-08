package com.example.demo.views;

import com.example.demo.service.LogInService;
import com.example.demo.service.SQLService;
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
import org.springframework.beans.factory.annotation.Autowired;

@Route("/login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

  @Autowired
  LogInService loginService;

  @Autowired
  SQLService sqlService;


  public LoginView() {

    TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");

    add(
        new H1("Log in to save your data, or click Quickstart"),
        username,
        password,
        new Button("Login to play against non-user", event -> {

          if(sqlService.checkLoginCredentials(username.getValue(), password.getValue())){
            loginService.setNameOfLoggedInUserOne(username.getValue());
            loginService.setPlayerOneLoggedIn(true);
            UI.getCurrent().navigate("/game");
          } else {
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