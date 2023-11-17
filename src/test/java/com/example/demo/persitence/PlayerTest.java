package com.example.demo.persitence;

import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PlayerTest {
    @Autowired
    PlayerService playerService;

    @Test
    public void advantageGivenAndTakenCorrectly(){
        PlayerDto playerOne = new PlayerDto();
        PlayerDto playerTwo = new PlayerDto();
        playerOne.gamesStorage.add(0);
        playerTwo.gamesStorage.add(0);
        scoredMultiplePoints(playerOne, playerTwo, 3);
        assertTrue(playerOne.isHasAdvantage() && !playerTwo.isHasAdvantage());
        playerService.pointScoringLogic(playerTwo, playerOne);
        assertFalse(!playerOne.isHasAdvantage() && !playerTwo.isHasAdvantage());
        scoredMultiplePoints(playerTwo, playerOne, 3);
        assertTrue(!playerOne.isHasAdvantage() && playerTwo.isHasAdvantage());
    }
    @Test
    public void gameWonWhenScoringDuringAdvantage(){
        PlayerDto playerOne = new PlayerDto();
        PlayerDto playerTwo = new PlayerDto();
        playerOne.gamesStorage.add(0);
        playerTwo.gamesStorage.add(0);
        scoredMultiplePoints(playerOne, playerTwo, 4);
        assertTrue(playerOne.getPoints() == 0 && playerOne.getGamesStorage().get(0) == 1
        && playerTwo.getPoints() == 0 && playerTwo.getGamesStorage().get(0) == 0);
    }

    private void scoredMultiplePoints(PlayerDto playerOne, PlayerDto playerTwo, int pointsScored){
        for(int i = 0; i < pointsScored; i++){
            playerService.pointScoringLogic(playerOne, playerTwo);
        }
    }

    @Test
    public void playerIsCreatedWithUsernameAlreadyInDatabaseButNotActivated() {
        //Player player = new Player("abcd", "abcd", "abcd@abcd.abcd");
    }
}