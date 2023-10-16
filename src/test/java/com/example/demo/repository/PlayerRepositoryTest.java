package com.example.demo.repository;


import com.example.demo.entity.Player;

import java.util.List;

import com.vaadin.flow.component.notification.Notification;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.helger.commons.mock.CommonsAssert.*;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void jokinger(){
        assertEquals("A", "A");
    }

    @Test
    public void abc(){
        Player player = new Player("hihuabc");
        playerRepository.save(player);
        assertEquals(playerRepository.findPlayerByName("hihuabc").getName(),"hihuabc");
    }
/*
    @Autowired
    private PlayerRepository playerRepository;


    @Test
    public void savePlayer() {
        Player player = new Player("testPlayer6");
        System.out.println(player.getId());
        Player player2 = new Player("testPlayer6");
        System.out.println(player2.getId());
        playerRepository.save(player2);
    }

    @Test
    public void printPlayerByNameContaining() {

        List<Player> players = playerRepository.findPlayerByNameContaining("er");
        System.out.println(players);
    }

    @Test
    public void findPlayerByEmail() {
        assertEquals(playerRepository.findPlayerByEmail("ferdinand.gutzy@gmx.de").getName(), "falkinger");
        assertEquals(playerRepository.findPlayerByEmail("ferdinand.gutzy@gmx.de") != null, true);
        assertEquals(playerRepository.findPlayerByEmail("ferdinand.gutzy99@gmx.de") == null, true);
    }
 */
}