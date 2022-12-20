package com.example.demo.service;

import com.vaadin.flow.component.button.Button;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogInService {

  @Autowired
  PlayerService playerService;

  @Getter
  @Setter
  String nameOfLoggedInUser="";

  @Getter
  @Setter
  boolean loginSuccesfull = false;

public void loginOrLogoutButtonClicked(Button loginLogoutButton) {

  if (playerService.loggedInOrNot){
    loginLogoutButton.setText("Log In");
  }else loginLogoutButton.setText("Log Out");

/*
  loginLogoutButton.addClickListener(event -> {
    if (gameService.isPlayerIsLoggedIn()) {
      VaadinSession.getCurrent().close();
      gameService.setPlayerIsLoggedIn(false);
    } else {
      UI.getCurrent().navigate("");
    }
  });
 */

}
}
