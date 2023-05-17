package com.example.demo.service;

import com.example.demo.views.GameView;
import com.example.demo.views.LeaderboardView;
import com.example.demo.views.MultiplayerView;
import com.example.demo.views.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import java.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SQLService {

  @Autowired
  LogInService logInService;

  public void countWinOrLoss(String winningPlayer, String loosingPlayer) throws SQLException {

    if (logInService.isPlayerOneLoggedIn()){

      Connection conn = DriverManager
          .getConnection("jdbc:mysql://localhost:3306/tennis_db", "root", "rootpassword");
      String updateSql =
          "UPDATE tbl_player SET games_played = games_played + 1, games_won = games_won + CASE WHEN name_of_player = ? THEN 1 ELSE 0 END, games_lost = games_lost +" +
              "CASE WHEN name_of_player = ? THEN 1 ELSE 0 END WHERE name_of_player IN (?,?)";
      try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
        updateStmt.setString(1, winningPlayer);
        updateStmt.setString(2, loosingPlayer);
        updateStmt.setString(3, winningPlayer);
        updateStmt.setString(4, loosingPlayer);
        updateStmt.executeUpdate();
      } catch (SQLException e) {
        System.out.println("Error while updating Data for winning Player");
      }
    }
  }



  public boolean checkLoginCredentials(String nameOfPlayerToCheck, String passwordToCheck) {

    boolean check = false;

    String sql = "SELECT * FROM tbl_player WHERE name_of_player = ? AND password = ?";
    try {
      Connection conn = DriverManager
          .getConnection("jdbc:mysql://localhost:3306/tennis_db", "root", "rootpassword");
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1, nameOfPlayerToCheck);
      statement.setString(2, passwordToCheck);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()){
        check = true;
      }
    } catch (SQLException e) {
      System.out.println("Error executing query: " + e.getMessage());
    }

    return check;
  }
}
