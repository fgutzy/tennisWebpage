package com.example.demo.repository;

import com.example.demo.entity.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

  //returns a List of Players that contain a passed String value
  public List<Player> findPlayerByNameContaining(String name);

  public Player findByName(String name);
}
