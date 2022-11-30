package com.example.demo;

import java.util.ArrayList;


public class Player {

  String name;
  int sets = 0;
  ArrayList<Integer> gamesStorage = new ArrayList<>();
  int games;
  int points = 0;
  boolean hasAdvantage;
  int currentGame = 0;
  boolean tiebreak = false;

  String printADOrNot = "";


  public Player(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
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

    //if player scored while having an Advantage
    if (this.hasAdvantage) {

      //after scoring during Advantage Points are set to zero
      setPointsOfBothPlayersZero(this, playerGettingScoredAgainst);

      //after game was won, add 1 game to his current gamescore
      this.gamesStorage.set(currentGame, this.gamesStorage.get(this.currentGame) + 1);

      checkIfTiebreak(this, playerGettingScoredAgainst);

      checkIfEnoughGamesForSet(this, playerGettingScoredAgainst);

      //Advantage is always set false and wil be checked for after every point scored
      setAdvantageFalse(this);
      return;
    }

    //if Player scores in a state where no Player has 6 games (bc that would mean its a Tiebreak)
    if (this.gamesStorage.get(currentGame) != 6 ||
        playerGettingScoredAgainst.gamesStorage.get(currentGame) != 6) {

      scoredWhileNoTiebreak(playerGettingScoredAgainst);

    } else {
      scoredwhileTiebreak(playerGettingScoredAgainst);
    }
  }

  private void scoredwhileTiebreak(Player playerGettingScoredAgainst){

    this.setPoints(this.getPoints() + 1);

    //if scoring player has 6 or more points and more than opponent he gets and advantage
    if (this.getPoints() >= 6 && this.getPoints() > playerGettingScoredAgainst.getPoints()) {
      setAdvantageTrue(this);
    } else if (this.getPoints() == playerGettingScoredAgainst.getPoints()) {
      setAdvantageFalse(playerGettingScoredAgainst);
    }

    //if scoring Player has 7 or more Points and a two Point gap to other Player he won the game
    if (this.getPoints() >= 7 && this.getPoints() > playerGettingScoredAgainst.getPoints() + 1) {
      setPointsOfBothPlayersZero(this, playerGettingScoredAgainst);
      tiebreak = false;
    }
  }

  private void scoredWhileNoTiebreak(Player playerGettingScoredAgainst){
    if (this.getPoints() == 40) {
      scoringwhileHavingFourtyPoints(playerGettingScoredAgainst);
    }

    if (this.getPoints() == 30) {
     scoringWhileHavingThirtyPoints(playerGettingScoredAgainst);
    }

    if (this.getPoints() <= 30) {
      this.setPoints(this.getPoints() + 15);
    }
  }

  private void scoringwhileHavingFourtyPoints(Player playerGettingScoredAgainst){
    if (playerGettingScoredAgainst.getPoints() != 40 ||
        !playerGettingScoredAgainst.hasAdvantage) {
      setAdvantageTrue(this);
    } else {
      setAdvantageFalse(playerGettingScoredAgainst);
    }
  }

  private void scoringWhileHavingThirtyPoints(Player playerGettingScoredAgainst){
    this.setPoints(40);
    setAdvantageFalse(playerGettingScoredAgainst);
    if (this.getPoints() > playerGettingScoredAgainst.getPoints()) {
      setAdvantageTrue(this);
    }
  }


  private void setAdvantageTrue(Player playerToSetAdvantageTrueFor){
    playerToSetAdvantageTrueFor.hasAdvantage = true;
    playerToSetAdvantageTrueFor.printADOrNot = " AD";
  }

  private void setAdvantageFalse(Player playerToSetAdvantageFalseFor){
    playerToSetAdvantageFalseFor.hasAdvantage = false;
    playerToSetAdvantageFalseFor.printADOrNot = " ";
  }

  private void checkIfEnoughGamesForSet(Player scoringPlayer, Player playerGettingScoredAgainst){
    int differenceInGames =
        Math.abs(
            scoringPlayer.gamesStorage.get(scoringPlayer.currentGame) - playerGettingScoredAgainst.gamesStorage.get(
                playerGettingScoredAgainst.currentGame));
    if (scoringPlayer.gamesStorage.get(currentGame) >= 6 && differenceInGames >= 2 ||
        scoringPlayer.gamesStorage.get(currentGame) == 7) {
      scoringPlayer.setSets(scoringPlayer.getSets() + 1);

      scoringPlayer.gamesStorage.add(0);
      playerGettingScoredAgainst.gamesStorage.add(0);

      playerGettingScoredAgainst.gamesStorage.set(currentGame,
          playerGettingScoredAgainst.gamesStorage.get(playerGettingScoredAgainst.currentGame));
      playerGettingScoredAgainst.currentGame += 1;
      scoringPlayer.currentGame += 1;
    }
  }


  private void checkIfTiebreak(Player scoringPlayer, Player playerGettingScoredAgainst){
    if (scoringPlayer.gamesStorage.get(currentGame) == 6 &&
        playerGettingScoredAgainst.gamesStorage.get(currentGame) == 6) {
      scoringPlayer.tiebreak = true;
      playerGettingScoredAgainst.tiebreak = true;
    } else {
      scoringPlayer.tiebreak = false;
      playerGettingScoredAgainst.tiebreak = false;
    }
  }

  private void setPointsOfBothPlayersZero(Player scoringPlayer, Player playerGettingScoredAgainst) {
    scoringPlayer.setPoints(0);
    playerGettingScoredAgainst.setPoints(0);
  }


  public String getScoreOfPlayer() {
    return this.getName() + " " + this.getSets() + " " +
        this.gamesStorage + " " +
        this.getPoints() +
        this.printADOrNot;
  }
}
