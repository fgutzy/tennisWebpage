package com.example.demo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.stereotype.Service;


@Service
public class SQLService {

  public void updatePlayerColumn(int playerOneId, String columnToChangeForPlayerOne,
                                 int newValueForPlayerOne, int playerTwoId, String columnToChangeForPlayerTwo,
                                 int newValueForPlayerTwo) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tennis_db", "root", "rootpassword")) {
      String updatePlayer1Sql = "UPDATE tbl_player SET " + columnToChangeForPlayerOne + " = ? WHERE id = ?";
      String updatePlayer2Sql = "UPDATE tbl_player SET " + columnToChangeForPlayerTwo + " = ? WHERE id = ?";
      String updateGamesPlayedSql = "UPDATE tbl_player SET games_played = games_played + 1 " + "WHERE id IN(?, ?)";

      //update the win or loss number for player one
      PreparedStatement updatePlayer1Stmt = conn.prepareStatement(updatePlayer1Sql);
      updatePlayer1Stmt.setInt(1, newValueForPlayerOne);
      updatePlayer1Stmt.setInt(2, playerOneId);
      updatePlayer1Stmt.executeUpdate();


      //update the win or loss number for player two
      PreparedStatement updatePlayer2Stmt = conn.prepareStatement(updatePlayer2Sql);
      updatePlayer2Stmt.setInt(1, newValueForPlayerTwo);
      updatePlayer2Stmt.setInt(2, playerTwoId);
      updatePlayer2Stmt.executeUpdate();


      //update the number of games played for both players
      PreparedStatement updateGamesPlayed = conn.prepareStatement(updateGamesPlayedSql);
      updateGamesPlayed.setInt(1, playerOneId);
      updateGamesPlayed.setInt(2, playerTwoId);
      updateGamesPlayed.executeUpdate();

    } catch (SQLException e) {
      // handle the exception
      e.printStackTrace();
    }
  }
}
