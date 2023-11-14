package com.example.demo.repository;


import com.example.demo.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.helger.commons.mock.CommonsAssert.assertEquals;

@SpringBootTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;
    @Test
    public void saveAndRetrievePlayer(){
        Player player = new Player("petri");
        playerRepository.save(player);
        assertEquals(playerRepository.findPlayerByName("petri").getName(),"petri");
    }
}