package com.example.demo.repository;

import com.example.demo.entity.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface
PlayerRepository extends JpaRepository<Player, Integer> {

  List<Player> findPlayerByNameContaining(String name);
  Player findPlayerByName(String name);
  Player findPlayerByNameAndPassword(String name, String password);
  Player findPlayerByValidationCode(String code);
  Player findPlayerByEmail(String email);
  @Modifying
  @Transactional
  @Query("UPDATE Player p SET p.password = :newPassword WHERE p.name = :name")
  void updatePasswordByName(String newPassword, String name);


  @Modifying
  @Transactional
  @Query("UPDATE Player p SET p.accountActivated = true WHERE p.name = :name")
  void updateAccountActivatedTrueByName(String name);
  @Modifying
  @Transactional
  @Query("UPDATE Player p SET p.validationCode = :validationCode WHERE p.name = :name")
  void updateValidationCodeByName(String validationCode, String name);

  @Modifying
  @Transactional
  @Query("UPDATE Player p SET p.email = :email WHERE p.name = :name")
  void updateEmailByName(String email, String name);

  @Modifying
  @Transactional
  @Query(value = "UPDATE tbl_player SET games_played = games_played + 1, " +
          "games_won = games_won + CASE WHEN name = ?1 THEN 1 ELSE 0 END, " +
          "games_lost = games_lost + CASE WHEN name = ?2 THEN 1 ELSE 0 END " +
          "WHERE name IN (?1, ?2)", nativeQuery = true)
  void countWinOrLoss(String winningPlayer, String losingPlayer);
}
