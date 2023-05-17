package com.example.demo.views;

import com.example.demo.entity.Match;
import com.example.demo.entity.Player;
import com.example.demo.repository.MatchHistoryRepository;
import com.example.demo.repository.PlayerRepository;
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
import java.sql.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route("register")
public class RegisterView extends Composite {

  @Autowired
  PlayerRepository playerRepository;

  @Autowired
  MatchHistoryRepository matchHistoryRepository;


  @Override
  protected Component initContent() {

    TextField username = new TextField("Username");
    PasswordField password1 = new PasswordField("Password");
    PasswordField password2 = new PasswordField("Confirm Password");


    VerticalLayout layout = new VerticalLayout(
        new H2("Register"),
        username,
        password1,
        password2,
        new Button("Done", event -> {
          try {
            register(
                username.getValue(),
                password1.getValue(),
                password2.getValue()
            );
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        })
    );
    layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    return layout;
  }


  private void register(String username, String password1, String password2)
      throws InterruptedException {

     // Validate username
      if (username.isEmpty() || username.contains(" ") || username.equalsIgnoreCase("admin")) {
        Notification.show("Invalid username");
        return;
      }
    // password must be equal and longer then 5 characters
    if (!password1.equals(password2) || password1.length() < 5) {
      Notification.show("Passwords don't match or are too short (min. 5 characters)");
      return;
    }

    // Check if username already exists in database
    if (playerRepository.findPlayerByName(username) != null){
      Notification.show("Username taken");
    } else {
      //save Player to Repository
      Player registeredPlayer = new Player(username, password1);
      playerRepository.save(registeredPlayer);

      Notification.show("Account succesfully created\n"+"You will be redirected in a second");
      Thread.sleep(1500);
      UI.getCurrent().navigate("/login");
    }
    }
}
