package com.example.demo.service;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {

  @Autowired
  PlayerRepository playerRepository;

  @Autowired
  PlayerService playerService;

  public void checkCredentials(String playerName, String passwordEntered) throws AuthException {

    Player playerEntered = playerRepository.findByName(playerName);

    if (playerEntered.getPassword().equals(passwordEntered)){
      playerService.setLoggedInOrNot(true);
    } else throw new AuthException("Wrong credentials");
  }

  public class AuthException extends Throwable {

    public AuthException(String message){
      super(message);
    }
  }


}
