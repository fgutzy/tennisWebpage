package com.example.demo.service;

import com.example.demo.persitence.Player;
import com.example.demo.persitence.repository.PlayerRepository;
import com.example.demo.service.dto.DtoFactory;
import com.example.demo.service.dto.PlayerDto;
import com.example.demo.service.dto.PlayerDataDto;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    public int currentGame = 0;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DtoFactory dtoFactory;


    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public void pointScored(PlayerDto playerOne, PlayerDto playerTwo, Paragraph tiebreakMessage) {

        pointScoringLogic(playerOne, playerTwo);

        checkForTiebreak(playerOne, tiebreakMessage);
    }

    public void pointScoringLogic(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {

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

    private void scoredwhileTiebreak(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {

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

    private void scoredWhileNoTiebreak(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {
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

    private void scoringwhileHavingFourtyPoints(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {
        if (playerGettingScoredAgainst.getPoints() != 40 ||
                !playerGettingScoredAgainst.isHasAdvantage()) {
            setAdvantageTrue(scoringPlayer);
        } else {
            setAdvantageFalse(playerGettingScoredAgainst);
        }
    }

    private void scoringWhileHavingThirtyPoints(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {
        scoringPlayer.setPoints(40);
        setAdvantageFalse(playerGettingScoredAgainst);
        if (scoringPlayer.getPoints() > playerGettingScoredAgainst.getPoints()) {
            setAdvantageTrue(scoringPlayer);
        }
    }


    private void setAdvantageTrue(PlayerDto playerToSetAdvantageTrueFor) {
        playerToSetAdvantageTrueFor.setHasAdvantage(true);
        playerToSetAdvantageTrueFor.setPrintADOrNot("AD");
    }

    private void setAdvantageFalse(PlayerDto playerToSetAdvantageFalseFor) {
        playerToSetAdvantageFalseFor.setHasAdvantage(false);
        playerToSetAdvantageFalseFor.setPrintADOrNot(" ");
    }

    private void checkIfEnoughGamesForSet(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {
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

    private void checkIfTiebreak(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {
        if (scoringPlayer.gamesStorage.get(currentGame) == 6 &&
                playerGettingScoredAgainst.gamesStorage.get(currentGame) == 6) {
            scoringPlayer.setTiebreak(true);
            playerGettingScoredAgainst.setTiebreak(true);
        } else {
            scoringPlayer.setTiebreak(false);
            playerGettingScoredAgainst.setTiebreak(false);
        }
    }

    private void setPointsOfBothPlayersZero(PlayerDto scoringPlayer, PlayerDto playerGettingScoredAgainst) {
        scoringPlayer.setPoints(0);
        playerGettingScoredAgainst.setPoints(0);
    }

    public void bringNamesToSameLength(PlayerDto playerOne, PlayerDto playerTwo) {
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


    public void removeLastGame(PlayerDto playerOne, PlayerDto playerTwo) {
        playerOne.gamesStorage.remove(playerOne.gamesStorage.size() - 1);
        playerTwo.gamesStorage.remove(playerTwo.gamesStorage.size() - 1);
    }

    public void checkForTiebreak(PlayerDto player, Paragraph paragraph) {
        paragraph.setText(player.isTiebreak() ? "Tiebreak" : "");
    }

    public List<PlayerDto> findAll(String filteredText) {
        List<PlayerDto> allDtoPlayers = new ArrayList<>();
        //return all Players if no filter was applied
        if (filteredText == null || filteredText.isEmpty()) {
            List<Player> allPlayers = playerRepository.findAll();
            for (Player player : allPlayers) {
                PlayerDto playerDto = dtoFactory.createDto(player);
                allDtoPlayers.add(playerDto);
            }
        } else { //else return the Player whos name is containing the filteredText value
            for (Player player : playerRepository.findPlayerByNameContaining(filteredText)) {
                allDtoPlayers.add(dtoFactory.createDto(player));
            }
        }
        return allDtoPlayers;
    }

    //misses to update the winning percentage
    public void countWinAndLoss(String winningPlayer, String loosingPlayer) {
        /*if (VaadinSession.getCurrent().getAttribute("playerOneLoggedIn").toString() != null &&
                VaadinSession.getCurrent().getAttribute("playerOneLoggedIn").toString().equals("true") &&
                VaadinSession.getCurrent().getAttribute("playerTwoLoggedIn").toString() != null &&
                VaadinSession.getCurrent().getAttribute("playerTwoLoggedIn").toString().equals("true")) {
            playerRepository.countWinOrLoss(winningPlayer.trim(), loosingPlayer.trim());
        }*/
        if(VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") != null &&
                VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserTwo") != null){
            playerRepository.countWinOrLoss(winningPlayer.trim(), loosingPlayer.trim());
        }
    }

    public PlayerDto findPlayerByValidationCode(String validationCode) {
        Player player = playerRepository.findPlayerByValidationCode(validationCode);
        return dtoFactory.createDto(player);
    }

    public PlayerDataDto findPlayerByEmail(String email) {
        Player player = playerRepository.findPlayerByEmail(email);
        if (player != null) {
            return dtoFactory.createPlayerDataDto(player);
        } else {
            return null;
        }
    }

    public PlayerDataDto findPlayerByNameAndPassword(String name, String password) {
        Player player = playerRepository.findPlayerByName(name);
        if (player != null && matchPassword(password, player.getPassword())) {
            return dtoFactory.createPlayerDataDto(player);
        } else {
            return null;
        }
    }

    public PlayerDto findPlayerByName(String name) {
        Player player = playerRepository.findPlayerByEmail(name);
        if (player != null) {
            return dtoFactory.createDto(player);
        } else {
            return null;
        }
    }

}





