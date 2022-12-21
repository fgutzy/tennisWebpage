package com.example.demo.repository;


import com.example.demo.entity.Player;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerRepositoryTest {

  @Autowired
      private PlayerRepository playerRepository;


  @Test
  public void savePlayer(){
    Player player = new Player("testPlayer6");
    playerRepository.save(player);
  }

  @Test
  public void printPlayerByNameContaining(){

    List<Player>players = playerRepository.findPlayerByNameContaining("er");
    System.out.println(players);
  }

}