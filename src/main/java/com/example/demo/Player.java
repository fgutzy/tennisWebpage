package com.example.demo;

import com.sun.jna.platform.mac.CoreFoundation;
import com.vaadin.flow.component.html.Paragraph;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


public class Player {

  String name;
  int sets = 0;
  ArrayList<Integer> gamesStorage = new ArrayList<>();
  int games;
  int points = 0;
  boolean hasAdvantage;
  int currentGame = 0;
  int currentServer;
  boolean playerOneWon = false;

  String printADOrNot = "";

  public Player(String name) {
    this.name = name;
  }


  public String getName() {
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public int getSets() {
    return sets;
  }

  public void setSets(int sets) {
    this.sets = sets;
  }

  public int getGames() {
    return games;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void scoredPointAgainst(Player playerGettingScoredAgainst) {

    setTrue();

    if (this.hasAdvantage){
      this.setPoints(0);
      playerGettingScoredAgainst.setPoints(0);
      //initialize gamesStorage with 0 for both players
      if (this.gamesStorage.isEmpty()) {
        this.gamesStorage.add(0);
        playerGettingScoredAgainst.gamesStorage.add(0);
      }
      this.gamesStorage.set(currentGame, this.gamesStorage.get(this.currentGame) + 1);
      if (this.gamesStorage.get(currentGame) == 3) {
        this.setSets(this.getSets() + 1);

        this.gamesStorage.add(0);
        playerGettingScoredAgainst.gamesStorage.add(0);
        playerGettingScoredAgainst.gamesStorage.set(currentGame,
            playerGettingScoredAgainst.gamesStorage.get(playerGettingScoredAgainst.currentGame));
        playerGettingScoredAgainst.currentGame += 1;
        this.currentGame += 1;

      }
      this.hasAdvantage = false;
      this.printADOrNot = "";
      return;
    }

    if (this.getPoints() == 40) {
      if (playerGettingScoredAgainst.getPoints() != 40 ||
          !playerGettingScoredAgainst.hasAdvantage) {
        this.hasAdvantage = true;
        this.printADOrNot = " AD";
      } else if (this.getPoints() == 40 && playerGettingScoredAgainst.hasAdvantage) {
        playerGettingScoredAgainst.hasAdvantage = false;
        playerGettingScoredAgainst.printADOrNot = "";
      }
    }

      if (this.getPoints() == 30){
        this.setPoints(40);
        playerGettingScoredAgainst.hasAdvantage = false;
        playerGettingScoredAgainst.printADOrNot = "";
        if (this.getPoints() > playerGettingScoredAgainst.getPoints()){
          this.hasAdvantage = true;
          this.printADOrNot = " AD";
        }
      }

      if (this.getPoints() <= 30){
        this.setPoints(this.getPoints()+15);
      }
  }


  public void checkIfSetIsWon() {
    if (this.getGames() == 3) {
      this.setSets(this.getSets() + 1);
    }
  }

  public void setTrue(){
    this.playerOneWon = true;
  }

  public String getScoreOfPlayer(Player player) {
    return  player.getName() + " " + player.getSets() + " " +
        player.gamesStorage + " " +
        player.getPoints() +
        player.printADOrNot;
  }
}
