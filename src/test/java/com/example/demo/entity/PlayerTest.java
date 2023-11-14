package com.example.demo.entity;

import com.example.demo.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PlayerTest {
/*
    @Autowired
    PlayerService playerService;


    @Test
    public void advantageGivenAndTakenCorrectly(){
        Player playerOne = new Player();
        Player playerTwo = new Player();
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
        Player playerOne = new Player();
        Player playerTwo = new Player();
        playerOne.gamesStorage.add(0);
        playerTwo.gamesStorage.add(0);
        scoredMultiplePoints(playerOne, playerTwo, 4);
        assertTrue(playerOne.getPoints() == 0 && playerOne.getGamesStorage().get(0) == 1);
    }

    private void scoredMultiplePoints(Player playerOne, Player playerTwo, int pointsScored){
        for(int i = 0; i < pointsScored; i++){
            playerService.pointScoringLogic(playerOne, playerTwo);
        }
    }

    @Test
    public void playerIsCreatedWithUsernameAlreadyInDatabaseButNotActivated() {
        Player player = new Player("abcd", "abcd", "abcd@abcd.abcd");

    }*/
}