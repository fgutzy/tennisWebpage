package com.example.demo;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
public class Player {

  @Id
  @Getter
  @Setter
  String name;

  public ArrayList<Integer> gamesStorage = new ArrayList<>();

  @Getter
  @Setter
  int sets = 0;

  @Getter
  @Setter
  int games;

  @Getter
  @Setter
  int points = 0;

  public boolean hasAdvantage;
  public boolean tiebreak = false;

  public String printADOrNot = "";


  public Player(String name) {
    this.name = name;
  }

  public String getScoreOfPlayer() {
    return this.getName() + " " + this.getSets() + " " +
        this.gamesStorage + " " +
        this.getPoints() +
        this.printADOrNot;
  }
}
