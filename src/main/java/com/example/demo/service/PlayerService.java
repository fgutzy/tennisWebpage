package com.example.demo.service;

import com.example.demo.Player;
import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.html.Paragraph;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {


  public int currentGame = 0;


  @Autowired
  private PlayerRepository playerRepository;

  public PlayerService() throws SQLException {
  }


  public void pointScored(Player playerOne, Player playerTwo, Paragraph tiebreakMessage){

    pointScoredInternal(playerOne, playerTwo);

    checkForTiebreak(playerOne, tiebreakMessage);
  }

  public void pointScoredInternal(Player scoringPlayer, Player playerGettingScoredAgainst) {

    //if player scored while having an Advantage
    if (scoringPlayer.hasAdvantage) {

      //after scoring during Advantage Points are set to zero
      setPointsOfBothPlayersZero(scoringPlayer, playerGettingScoredAgainst);

      //after game was won, add 1 game to his current gamescore
      scoringPlayer.gamesStorage
          .set(currentGame, scoringPlayer.gamesStorage.get(currentGame) + 1);

      checkIfTiebreak(scoringPlayer, playerGettingScoredAgainst);

      checkIfEnoughGamesForSet(scoringPlayer, playerGettingScoredAgainst);

      //Advantage is always set false and wil be checked for after every point scored
      setAdvantageFalse(scoringPlayer);
      return;
    }
    //if Player scores in a state where no Player has 6 games (bc that would mean its a Tiebreak)
    if (scoringPlayer.gamesStorage.get(currentGame) != 6 ||
        playerGettingScoredAgainst.gamesStorage.get(currentGame) != 6) {

      scoredWhileNoTiebreak(scoringPlayer, playerGettingScoredAgainst);

    } else {
      scoredwhileTiebreak(scoringPlayer, playerGettingScoredAgainst);
    }
  }

  private void scoredwhileTiebreak(Player scoringPlayer, Player playerGettingScoredAgainst){

    scoringPlayer.setPoints(scoringPlayer.getPoints() + 1);

    //if scoring player has 6 or more points and more than opponent he gets and advantage
    if (scoringPlayer.getPoints() >= 6 && scoringPlayer.getPoints() > playerGettingScoredAgainst.getPoints()) {
      setAdvantageTrue(scoringPlayer);
    } else if (scoringPlayer.getPoints() == playerGettingScoredAgainst.getPoints()) {
      setAdvantageFalse(playerGettingScoredAgainst);
    }

    //if scoring Player has 7 or more Points and a two Point gap to other Player he won the game
    if (scoringPlayer.getPoints() >= 7 && scoringPlayer.getPoints() > playerGettingScoredAgainst.getPoints() + 1) {
      setPointsOfBothPlayersZero(scoringPlayer, playerGettingScoredAgainst);
      scoringPlayer.tiebreak = false;
    }
  }

  private void scoredWhileNoTiebreak(Player scoringPlayer, Player playerGettingScoredAgainst){
    if (scoringPlayer.getPoints() == 40) {
      scoringwhileHavingFourtyPoints(scoringPlayer, playerGettingScoredAgainst);
    }

    if (scoringPlayer.getPoints() == 30) {
      scoringWhileHavingThirtyPoints(scoringPlayer, playerGettingScoredAgainst);
    }

    if (scoringPlayer.getPoints() <= 30) {
      scoringPlayer.setPoints(scoringPlayer.getPoints() + 15);
    }
  }

  private void scoringwhileHavingFourtyPoints(Player scoringPlayer, Player playerGettingScoredAgainst){
    if (playerGettingScoredAgainst.getPoints() != 40 ||
        !playerGettingScoredAgainst.hasAdvantage) {
      setAdvantageTrue(scoringPlayer);
    } else {
      setAdvantageFalse(playerGettingScoredAgainst);
    }
  }

  private void scoringWhileHavingThirtyPoints(Player scoringPlayer, Player playerGettingScoredAgainst){
    scoringPlayer.setPoints(40);
    setAdvantageFalse(playerGettingScoredAgainst);
    if (scoringPlayer.getPoints() > playerGettingScoredAgainst.getPoints()) {
      setAdvantageTrue(scoringPlayer);
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
            scoringPlayer.gamesStorage.get(currentGame) - playerGettingScoredAgainst.gamesStorage.get(
               currentGame));
    if (scoringPlayer.gamesStorage.get(currentGame) >= 6 && differenceInGames >= 2 ||
        scoringPlayer.gamesStorage.get(currentGame) == 7) {
      scoringPlayer.setSets(scoringPlayer.getSets() + 1);

      scoringPlayer.gamesStorage.add(0);
      playerGettingScoredAgainst.gamesStorage.add(0);

      playerGettingScoredAgainst.gamesStorage.set(currentGame,
          playerGettingScoredAgainst.gamesStorage.get(currentGame));
      currentGame += 1;
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

  public void bringNamesToSameLength(Player playerOne, Player playerTwo){
    StringBuilder toChange;

    if (playerOne.getName().length() != playerTwo.getName().length()) {

      int lengthDifference =
          Math.abs(playerOne.getName().length() - playerTwo.getName().length()) * 2 - 2;

      if (playerOne.getName().length() < playerTwo.getName().length()) {
        toChange = new StringBuilder(playerOne.getName());
        playerOne.setName(toChange.append("\u00a0".repeat(Math.max(0, lengthDifference))).toString());
      } else {
        toChange = new StringBuilder(playerTwo.getName());
        playerTwo.setName(toChange.append("\u00a0".repeat(Math.max(0, lengthDifference))).toString());
      }
    }
  }

  public void removeLastGame(Player playerOne, Player playerTwo){
    playerOne.gamesStorage.remove(playerOne.gamesStorage.size() - 1);
    playerTwo.gamesStorage.remove(playerTwo.gamesStorage.size() - 1);
  }

  public void checkForTiebreak(Player player, Paragraph paragraph){
    if (player.tiebreak){
      paragraph.setText("Tiebreak!");
    }else {
      paragraph.setText("");
    }
  }

  public List<Player> findAll(String filteredText) {

    //return all Players if no filter was applied
    if (filteredText == null || filteredText.isEmpty()){
      return playerRepository.findAll();
    }

    else{ //else return the Player whos name is containing the filteredText value
      return playerRepository.findPlayerByNameContaining(filteredText);
    }
  }

  /**
   * Method to count wins and loses of a player
   */

  public void countWinOrLoss(Player winningPlayer, Player losingPlayer){

    winningPlayer.setGamesPlayed(winningPlayer.getGamesPlayed()+1);
    losingPlayer.setGamesPlayed(losingPlayer.getGamesPlayed()+1);

    winningPlayer.setGamesWon(+1);
    losingPlayer.setGamesLost(+1);
  }
  }



