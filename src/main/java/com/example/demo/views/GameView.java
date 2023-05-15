package com.example.demo.views;

import com.example.demo.entity.Match;
import com.example.demo.entity.Player;
import com.example.demo.repository.MatchHistoryRepository;
import com.example.demo.service.GameService;
import com.example.demo.service.LogInService;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/game")
@PageTitle("Game")
public class GameView extends VerticalLayout {

  boolean startClick = true;


  PlayerService playerService;

  LogInService logInService;

  GameService gameService;

  @Autowired
  MatchHistoryRepository matchHistoryRepository;


  Paragraph tiebreakMessage;
  IntegerField buttonChoosingSetsNeededToWin;
  Button playerOneButton;
  Button playerTwoButton;
  Button startOrEndButton;
  Label scoreLabelPlayerOne;
  Label scoreLabelPlayerTwo;

  public GameView(LogInService logInService, PlayerService playerService, GameService gameService, MatchHistoryRepository matchHistoryRepository) {
    this.logInService = logInService;
    this.playerService = playerService;
    this.gameService = gameService;
    this.matchHistoryRepository = matchHistoryRepository;

    Button loginLogoutButton = new Button();
    if (!logInService.isPlayerOneLoggedIn()){
      loginLogoutButton.setText("Log In");
    }else loginLogoutButton.setText("Log Out");

    loginLogoutButton.addClickListener(event -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      UI.getCurrent().navigate("/login");
      logInService.setNameOfLoggedInUserOne("");
      logInService.setNameOfLoggedInUserTwo("");
      logInService.setPlayerOneLoggedIn(false);
      logInService.setPlayerTwoLoggedIn(false);

    });

    Button goToLeaderboard = new Button("Leaderboard", e->
        UI.getCurrent().navigate("/leaderboard")
    );

    VerticalLayout header1 = new VerticalLayout(goToLeaderboard);
    VerticalLayout header = new VerticalLayout(loginLogoutButton);
    HorizontalLayout a = new HorizontalLayout();
    a.add(header, header1);
    add(a);


    //create Message but dont initialize (will be done in the method that checks if tiebreak is happening)
    tiebreakMessage = new Paragraph("");


    //create all needed Fields and Buttons
    scoreLabelPlayerOne = new Label();
    scoreLabelPlayerTwo = new Label();

    //if logged in, take user name and set field disabled
    var playerOneNameField = new TextField("Enter name of player one");
    var playerTwoNameField = new TextField("Enter name of Player two");

    gameService.setNameFields(playerOneNameField, playerTwoNameField);

    playerOneNameField.setMaxLength(16);
    playerOneNameField.setHelperText("Max 16 letters");
    playerTwoNameField.setMaxLength(16);
    playerTwoNameField.setHelperText("Max 16 letters");

    startOrEndButton = new Button("Start Game"); // initialize button as Start
    startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

    playerOneButton = new Button(playerOneNameField.getValue());
    playerTwoButton = new Button(playerTwoNameField.getValue());


    //create players but set name after start button was pressed (cant take value from name field without refresh)
    Player playerOne = new Player("");
    Player playerTwo = new Player("");


    buttonChoosingSetsNeededToWin = new IntegerField();
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

  //  buttonLogic(playerOneButton, playerOne, playerTwo);

    //when button of player is pressed, call scorePoint method and set score for both players
    playerOneButton.addClickListener(o -> {

      playerService.pointScored(playerOne, playerTwo, tiebreakMessage);

      if (playerOne.getSets() ==
          buttonChoosingSetsNeededToWin.getValue()) { //check if enough sets to win the game

        //setting final sets is not correct and should only work if logged in //match can consits out of multiple sets
        //Match is created after start game button. after every set, match adds gameScore
        if (logInService.isPlayerOneLoggedIn() && logInService.isPlayerTwoLoggedIn()) {
          Match match = new Match(playerOne.getName(), playerTwo.getName(), displayFinalScore(playerOne, playerTwo));
          matchHistoryRepository.save(match);
        }

        //deacitvates all fields and updates the according result in SQL
        gameService.setValuesToEndGame(playerOne, playerOneButton, playerTwo, playerTwoButton,
            startOrEndButton, buttonChoosingSetsNeededToWin);

        //updating wins, loses and games played in SQL
        try {
          playerService.countWinOrLoss(logInService.getNameOfLoggedInUserOne(), logInService.getNameOfLoggedInUserTwo());
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }

        var playerWonMessage = new Paragraph(playerOne.getName() + " has won");
        add(playerWonMessage);
      }

      //set score for players after check if game wis won
      gameService.getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);

    });



    playerTwoButton.addClickListener(o -> {

      playerService.pointScored(playerTwo, playerOne, tiebreakMessage);

      if (playerTwo.getSets() ==
          buttonChoosingSetsNeededToWin.getValue()) { //check if enough sets to win the match

        //deacitvates all fields and updates the according result in SQL
        gameService.setValuesToEndGame(playerTwo, playerTwoButton, playerOne, playerTwoButton,
            startOrEndButton, buttonChoosingSetsNeededToWin);

        //updating wins, loses and games played in SQL
        try {
          playerService.countWinOrLoss(logInService.getNameOfLoggedInUserTwo(), logInService.getNameOfLoggedInUserOne());
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }

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

  //-1 needed bc after every won set, gamesStorage + 0
  String displayFinalScore(Player playerOne, Player playerTwo){
    String message = "";
    for (int i = 0; i < playerOne.gamesStorage.size()-1; i++){
      message += playerOne.gamesStorage.get(i) + ":" + playerTwo.gamesStorage.get(i) + " ";
    }
    return message;
  }


  void buttonLogic(Button playerButton, Player playerOne, Player playerTwo){
    //when button of player is pressed, call scorePoint method and set score for both players
    playerButton.addClickListener(o -> {

      playerService.pointScored(playerOne, playerTwo, tiebreakMessage);

      if (playerOne.getSets() ==
              buttonChoosingSetsNeededToWin.getValue()) { //check if enough sets to win the game

        //setting final sets is not correct and should only work if logged in //match can consits out of multiple sets
        //Match is created after start game button. after every set, match adds gameScore
        if (logInService.isPlayerOneLoggedIn() && logInService.isPlayerTwoLoggedIn()) {
          Match match = new Match(playerOne.getName(), playerTwo.getName(), displayFinalScore(playerOne, playerTwo));
          matchHistoryRepository.save(match);
        }

        //deacitvates all fields and updates the according result in SQL
        gameService.setValuesToEndGame(playerOne, playerOneButton, playerTwo, playerTwoButton,
                startOrEndButton, buttonChoosingSetsNeededToWin);

        //updating wins, loses and games played in SQL
        try {
          playerService.countWinOrLoss(logInService.getNameOfLoggedInUserOne(), logInService.getNameOfLoggedInUserTwo());
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }

        var playerWonMessage = new Paragraph(playerOne.getName() + " has won");
        add(playerWonMessage);
      }

      //set score for players after check if game wis won
      gameService.getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);

    });
  }
}





