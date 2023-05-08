package com.example.demo.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class LogInService {

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

}

