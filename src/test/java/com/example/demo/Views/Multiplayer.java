package com.example.demo.Views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class Multiplayer {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerService playerService;

    @Test
    public void loginMultiplayer() {
        String returner = "";
        boolean playerOneLoggedIn = checkCredentials("gutzy", "12345");
        boolean playerTwoLoggedIn = checkCredentials("gutzy2", "12345");

        if (playerOneLoggedIn && playerTwoLoggedIn) {
            returner = "both players logged in";
        } else {
            if (!playerOneLoggedIn) {
                returner = "wrong credentials for player one";
            }
            if (!playerTwoLoggedIn) {
                returner += "wrong credentials for player two";
            }
        }
        assertEquals("both players logged in", returner);
    }

    @Test
    public void updateWinsAndLosses() {
        Player playerOne = playerRepository.findPlayerByName("gutzyyy");
        Player playerTwo = playerRepository.findPlayerByName("gutzy2");

        int originalAmountGamesWon = playerOne.getGamesWon();
        int orginalAmountGamesPlayed = playerOne.getGamesPlayed();

        int originalAmountGamesLost = playerTwo.getGamesLost();
        int originalAmountGamesPlayed = playerTwo.getGamesPlayed();

        playerRepository.countWinOrLoss("gutzyyy", "gutzy2  ");

        playerOne = playerRepository.findPlayerByName("gutzyyy");
        playerTwo = playerRepository.findPlayerByName("gutzy2");

        assertTrue(originalAmountGamesWon +1 == playerOne.getGamesPlayed()
        && orginalAmountGamesPlayed +1 == playerOne.getGamesWon()
        && originalAmountGamesPlayed +1 == playerTwo.getGamesPlayed()
        && originalAmountGamesLost +1 == playerTwo.getGamesLost());
    }

    private boolean checkCredentials(String username, String password) {
        return playerRepository.findByNameAndAccountActivatedIsTrue(username) != null
                && playerService.matchPassword(password, playerRepository.findPlayerByName(username).getPassword());
    }
}
