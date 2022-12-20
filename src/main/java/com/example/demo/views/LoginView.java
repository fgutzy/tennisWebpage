package com.example.demo.views;

import com.example.demo.Game;
import com.example.demo.service.LogInService;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import java.sql.*;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/login")
@PageTitle("Login")

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  LoginForm loginForm = new LoginForm();

  @Autowired
  LogInService loginService;

  @Autowired
  PlayerService playerService;


  public LoginView() {

    var username = new TextField("Username");
    var password = new PasswordField("Password");

    add(
        new H1("Log in to save your data, or click Quickstart"),
        username,
        password,
        new Button("Login", event -> {

          String sql = "SELECT * FROM tbl_player WHERE name_of_player = ? AND password = ?";

          try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tennis_db", "root", "rootpassword");
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username.getValue());
            statement.setString(2, password.getValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) { // username fits password
              //String columnValue = resultSet.getString("name_of_player");
              loginService.setNameOfLoggedInUser(username.getValue());
              loginService.setLoginSuccesfull(true);
              playerService.setLoggedInOrNot(true);
              System.out.println(loginService.isLoginSuccesfull());
              //game.handleRequest();
              UI.getCurrent().navigate("/game");
            } else {
              Notification.show("Wrong credentials");
            }
          }

          catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
          }
        }),


        new RouterLink("Register", RegisterView.class),
        new RouterLink("Quickstart", Game.class)
    );

    setJustifyContentMode(JustifyContentMode.CENTER);
    setAlignItems(Alignment.CENTER);
  }


  public void setNameField(TextField playerOneNameField, String nameToSet) {
    if (nameToSet.equals("username")) {
      playerOneNameField.setValue("Enter name of Player One");
    } else
      playerOneNameField.setValue(nameToSet);
      playerOneNameField.setEnabled(false);
  }


  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (!event.getLocation()
      .getQueryParameters()
      .getParameters()
      .getOrDefault("error", Collections.emptyList())
      .isEmpty()){
        loginForm.setError(true);
    }
  }

}