package com.example.demo.views;

import com.example.demo.entity.Match;
import com.example.demo.repository.MatchHistoryRepository;
import com.example.demo.service.GameService;
import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDto;
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
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/game")
@PageTitle("Game")
public class GameView extends VerticalLayout {

  boolean startClick = true;

  PlayerService playerService;

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

  //create players but set name after start button was pressed (cant take value from name field without refresh)
  PlayerDto playerOne = new PlayerDto();
  PlayerDto playerTwo = new PlayerDto();

  public GameView(PlayerService playerService, GameService gameService, MatchHistoryRepository matchHistoryRepository) {
    this.playerService = playerService;
    this.gameService = gameService;
    this.matchHistoryRepository = matchHistoryRepository;

    Button loginLogoutButton = new Button();

    loginLogoutButton.setText(VaadinSession.getCurrent().getAttribute("playerOneLoggedIn") == null||
            !(boolean) VaadinSession.getCurrent().getAttribute("playerOneLoggedIn") ? "Log In": "Log Out");

    loginLogoutButton.addClickListener(event -> {
      if(loginLogoutButton.getText().equals("Log In")){
        UI.getCurrent().navigate("/login");
      }else {
        VaadinSession.getCurrent().getSession().invalidate();
      }
      VaadinSession.getCurrent().setAttribute("playerOneLoggedIn", false);
      VaadinSession.getCurrent().setAttribute("playerTwoLoggedIn", false);
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

      //method for filling up length differences in names to be more esthetic
      gameService.setPlayerNames(playerOne, playerOneNameField, playerTwo, playerTwoNameField);

      //after game is started change the buttons text, colour and display the score (0,0),
      // and create buttons with name of players
      if (startClick) {

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

    playerOneButton.addClickListener(o -> {
      buttonScoringLogic(playerOne, playerTwo);
      gameService.getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);
    });

    playerTwoButton.addClickListener(o ->{
      buttonScoringLogic(playerTwo, playerOne);
      gameService.getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);
    });

    add(playerAndSetInputFields, alignedStartButton);
    setAlignItems(Alignment.CENTER);
  }

  //-1 needed bc after every won set, gamesStorage + 0
  String displayFinalScore(PlayerDto playerOne, PlayerDto playerTwo){
    StringBuilder message = new StringBuilder();
    for (int i = 0; i < playerOne.gamesStorage.size()-1; i++){
      message.append(playerOne.gamesStorage.get(i)).append(":").append(playerTwo.gamesStorage.get(i)).append(" ");
    }
    return message.toString();
  }

  void buttonScoringLogic(PlayerDto scoringPlayer, PlayerDto otherPlayer){
    playerService.pointScored(scoringPlayer, otherPlayer, tiebreakMessage);

    if (scoringPlayer.getSets() ==
            buttonChoosingSetsNeededToWin.getValue()) { //check if enough sets to win the game

      Match match = new Match(scoringPlayer.getName(), otherPlayer.getName(), displayFinalScore(scoringPlayer, otherPlayer));
        matchHistoryRepository.save(match);

        //updating wins, loses and games played in SQL and for each Entity (for winning percentage)
      playerService.countWinAndLoss(scoringPlayer.getName(), otherPlayer.getName());

      //deactivates all fields and updates the according result in SQL
      gameService.setValuesToEndGame(scoringPlayer, playerOneButton, otherPlayer, playerTwoButton,
              startOrEndButton, buttonChoosingSetsNeededToWin);

      var playerWonMessage = new Paragraph(scoringPlayer.getName() + " has won");
      add(playerWonMessage);
    }
  }
}





