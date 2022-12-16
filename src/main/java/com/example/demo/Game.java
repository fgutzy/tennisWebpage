package com.example.demo;

import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.GameService;
import com.example.demo.service.PlayerService;
import com.example.demo.service.SQLService;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dialog.Dialog;
import javax.sql.DataSource;
import java.sql.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class Game extends VerticalLayout {

  boolean startClick = true;

  @Autowired
  PlayerService playerService;

  @Autowired
  GameService gameService;

  @Autowired
  PlayerRepository playerRepository;

  @Autowired
  SQLService sqlService;


  public Game() throws SQLException {


    //create Message but dont initialize (will be done in the method that checks if tiebreak is happening)
    var tiebreakMessage = new Paragraph("");

    //create all needed Fields and Buttons
    Label scoreLabelPlayerOne = new Label();
    Label scoreLabelPlayerTwo = new Label();

    var playerOneNameField = new TextField("Enter name of Player one");
    var playerTwoNameField = new TextField("Enter name of Player two");
    playerOneNameField.setMaxLength(16);
    playerOneNameField.setHelperText("Max 16 letters");
    playerTwoNameField.setMaxLength(16);
    playerTwoNameField.setHelperText("Max 16 letters");

    var startOrEndButton = new Button("Start Game"); // initialize button as Start
    startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

    var playerOneButton = new Button(playerOneNameField.getValue());
    var playerTwoButton = new Button(playerTwoNameField.getValue());


    //create players but set name after start button was pressed (cant take value from name field without refresh)
    Player playerOne = new Player();
    Player playerTwo = new Player("");


    IntegerField buttonChoosingSetsNeededToWin = new IntegerField();
    buttonChoosingSetsNeededToWin.setLabel("Sets needed to win");
    buttonChoosingSetsNeededToWin.setHelperText("Can be changed in-game");
    buttonChoosingSetsNeededToWin.setWidth("180px");
    buttonChoosingSetsNeededToWin.setMin(1);
    buttonChoosingSetsNeededToWin.setMax(9);
    buttonChoosingSetsNeededToWin.setValue(2);
    buttonChoosingSetsNeededToWin.setHasControls(true);

    // put fields and buttons in a variable
    var playerAndSetInputFields =
        new HorizontalLayout(playerOneNameField, playerTwoNameField, buttonChoosingSetsNeededToWin);
    var alignedStartButton = new HorizontalLayout(startOrEndButton); //button in new line

    //allign items accordingly
    playerAndSetInputFields.setDefaultVerticalComponentAlignment(Alignment.AUTO);
    alignedStartButton.setVerticalComponentAlignment(Alignment.CENTER);


/*    String url = "jdbc:mysql://localhost:3306/tennis_db";
    String username = "root";
    String password = "rootpassword";

    Connection conn = DriverManager.getConnection(url, username, password);

 */





    //startOrEndButton is clicked
    startOrEndButton.addClickListener(e -> {

      gameService.setPlayerNames(playerOne, playerOneNameField, playerTwo, playerTwoNameField);


      //after game is started change the buttons text, colour and display the score (0,0),
      // and create buttons with name of players
      if (startClick) {

        //method for filling up length differences in names to be more esthetic

        gameService
            .setVariablesReadyForGame(playerOne, playerOneNameField, playerOneButton, playerTwo,
                playerTwoNameField,
                playerTwoButton, startOrEndButton, scoreLabelPlayerOne, scoreLabelPlayerTwo);

        playerRepository.save(playerOne);
        playerRepository.save(playerTwo);

        gameService.winnerList.add(playerOne);

        var displayScore = new VerticalLayout(scoreLabelPlayerOne, scoreLabelPlayerTwo);
        var playOngoing = new HorizontalLayout(playerOneButton, playerTwoButton);
        displayScore.setAlignItems(Alignment.CENTER);
        add(displayScore, playOngoing, tiebreakMessage);

        startClick = false;

      } else { //if startOrEndButton is clicked after game was started change show pop up to make sure user wants to end

        Dialog dialog = new Dialog();
        gameService.setDialog(dialog);
        add(dialog);
      }


    });

    //when button of player is pressed, call scorePoint method and set score for both players
    playerOneButton.addClickListener(o -> {

      playerService.pointScored(playerOne, playerTwo, tiebreakMessage);

      if (playerOne.getSets() ==
          buttonChoosingSetsNeededToWin.getValue()) { //check if enough sets to win the game

        //deacitvates all fields and updates the according result in SQL
        gameService.setValuesToEndGame(playerOne, playerOneButton, playerTwo, playerTwoButton,
            startOrEndButton, buttonChoosingSetsNeededToWin);

        //testing Phase to store value of how many games played
        playerService.countWinOrLoss(playerOne, playerTwo);

        var playerWonMessage = new Paragraph(playerOne.getName() + " has won");
        add(playerWonMessage);
      }

      //set score for players after check if game wis won
      gameService.getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);


    });

    playerTwoButton.addClickListener(o -> {

      playerService.pointScored(playerTwo, playerOne, tiebreakMessage);

      if (playerTwo.getSets() ==
          buttonChoosingSetsNeededToWin.getValue()) { //check if enough sets to win the game

        //deacitvates all fields and updates the according result in SQL
        gameService.setValuesToEndGame(playerTwo, playerTwoButton, playerOne, playerTwoButton,
            startOrEndButton, buttonChoosingSetsNeededToWin);


        //create Message that Player won
        var playerWonMessage = new Paragraph(playerTwo.getName() + " has won");
        add(playerWonMessage);

      }

      //set score for players after check if game is won
      gameService.getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);
    });

    add(playerAndSetInputFields, alignedStartButton);
    setAlignItems(Alignment.CENTER);
  }

}



