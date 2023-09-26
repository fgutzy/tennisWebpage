package com.example.demo.service;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

  public int currentGame = 0;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public String hashPassword(String password) {
    return passwordEncoder.encode(password);
  }

  public boolean matchPassword(String password, String password2){
    return passwordEncoder.matches(password, password2);
  }


  public void pointScored(Player playerOne, Player playerTwo, Paragraph tiebreakMessage){

    pointScoringLogic(playerOne, playerTwo);

    checkForTiebreak(playerOne, tiebreakMessage);
  }

  public void pointScoringLogic(Player scoringPlayer, Player playerGettingScoredAgainst) {

    //if player scored while having an Advantage
    if (scoringPlayer.isHasAdvantage()) {

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
      scoringPlayer.setTiebreak(false);
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
        !playerGettingScoredAgainst.isHasAdvantage()) {
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
    playerToSetAdvantageTrueFor.setHasAdvantage(true);
    playerToSetAdvantageTrueFor.setPrintADOrNot("AD");
  }

  private void setAdvantageFalse(Player playerToSetAdvantageFalseFor){
    playerToSetAdvantageFalseFor.setHasAdvantage(false);
    playerToSetAdvantageFalseFor.setPrintADOrNot(" ");
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
      scoringPlayer.setTiebreak(true);
      playerGettingScoredAgainst.setTiebreak(true);
    } else {
      scoringPlayer.setTiebreak(false);
      playerGettingScoredAgainst.setTiebreak(false);
    }
  }

  private void setPointsOfBothPlayersZero(Player scoringPlayer, Player playerGettingScoredAgainst) {
    scoringPlayer.setPoints(0);
    playerGettingScoredAgainst.setPoints(0);
  }

  public void bringNamesToSameLength(Player playerOne, Player playerTwo){
    int lengthDifference = Math.abs(playerOne.getName().length() - playerTwo.getName().length());

    if (lengthDifference > 0) {
      String spaces = "\u00a0".repeat(lengthDifference);

      if (playerOne.getName().length() < playerTwo.getName().length()) {
        playerOne.setName(playerOne.getName() + spaces);
      } else {
        playerTwo.setName(playerTwo.getName() + spaces);
      }
    }
  }



  public void removeLastGame(Player playerOne, Player playerTwo){
    playerOne.gamesStorage.remove(playerOne.gamesStorage.size() - 1);
    playerTwo.gamesStorage.remove(playerTwo.gamesStorage.size() - 1);
  }

  public void checkForTiebreak(Player player, Paragraph paragraph){
    paragraph.setText(player.isTiebreak() ? "Tiebreak" : "");
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

  //misses to update th winning percentage
  public void countWinAndLoss(String winningPlayer, String loosingPlayer) {
    if((boolean) VaadinSession.getCurrent().getAttribute("playerOneLoggedIn")
    && (boolean) VaadinSession.getCurrent().getAttribute("playerTwoLoggedIn"))
      playerRepository.countWinOrLoss(winningPlayer, loosingPlayer);
      }
  }





