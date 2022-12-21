package com.example.demo.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class LogInService {

/*
  @Autowired
  PlayerService playerService;

 */

  @Getter
  @Setter
  String nameOfLoggedInUserOne="";

  @Getter
  @Setter
  String nameOfLoggedInUserTwo="";

  @Getter
  @Setter
  boolean playerOneLoggedIn = false;

  @Getter
  @Setter
  boolean playerTwoLoggedIn = false;

/*
  public void loginOrLogoutButtonClicked(Button loginLogoutButton) {

  if (loginSuccesfull){
    loginLogoutButton.setText("Log In");
  }else loginLogoutButton.setText("Log Out");


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

