package com.example.demo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;

@Route("")
public class Game extends VerticalLayout {

  boolean firstClickOnStartOrEndButton = true;
  boolean gameWon = false;

  public Game() {

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
    Player playerOne = new Player("");
    Player playerTwo = new Player("");

    IntegerField integerField = new IntegerField();
    integerField.setLabel("Sets needed to win");
    integerField.setHelperText("Can be changed in-game");
    integerField.setWidth("180px");
    integerField.setMin(1);
    integerField.setMax(9);
    integerField.setValue(2);
    integerField.setHasControls(true);

    // put fields and buttons in a variable
    var playerAndSetInputFields =
        new HorizontalLayout(playerOneNameField, playerTwoNameField, integerField);
    var alignedStartButton = new HorizontalLayout(startOrEndButton); //button in new line

    //allign items accordingly
    playerAndSetInputFields.setDefaultVerticalComponentAlignment(Alignment.AUTO);
    alignedStartButton.setVerticalComponentAlignment(Alignment.CENTER);


    //startOrEndButton is clicked
    startOrEndButton.addClickListener(e -> {

      //set Player names with input from textfields
      playerOne.setName(playerOneNameField.getValue());
      playerTwo.setName(playerTwoNameField.getValue());

      //method for filling up length differences in names to be more esthetic
      bringNamesToSameLength(playerOne, playerTwo);

      //after game is started change the buttons text, colour and display the score (0,0),
      // and create buttons with name of players
      if (firstClickOnStartOrEndButton) {

        startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        startOrEndButton.setText("End Game");

        getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);

        var displayScore = new VerticalLayout(scoreLabelPlayerOne, scoreLabelPlayerTwo);
        var playOngoing = new HorizontalLayout(playerOneButton, playerTwoButton);
        displayScore.setAlignItems(Alignment.CENTER);
        add(displayScore ,playOngoing, tiebreakMessage);

        playerOneButton.setText(playerOneNameField.getValue());
        playerTwoButton.setText(playerTwoNameField.getValue());

        //initialize gamesStorage with 0 for both players
        playerOne.gamesStorage.add(0);
        playerTwo.gamesStorage.add(0);


        firstClickOnStartOrEndButton = false;

      } else { //if startOrEndButton is clicked after game was started change show pop up to make sure user wants to end

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("End Game?");

        Button endGameButton = new Button("Yes, End Game", g -> UI.getCurrent().getPage().reload());
        Button cancelButton = new Button("Cancel", i -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(endGameButton);
        dialog.open();

        add(dialog);
      }

      //deactivate buttons for name input after game is started
      setPlayerFieldsFalse(playerOneNameField, playerTwoNameField);
    });

    //when cutton of player is pressed, call scorePoint method and set score for both players
    playerOneButton.addClickListener(o -> {
      playerOne.scoredPointAgainst(playerTwo);

      checkForTiebreak(playerOne, tiebreakMessage);

      if (playerOne.getSets() == integerField.getValue()) { //check if enough sets to win the game

        gameWon = true;

        //remove last object (bc 0 was added after game was over)
        removeLastGame(playerOne, playerTwo);

        var playerWonMessage = new Paragraph(playerOne.getName() + " has won");
        add(playerWonMessage);

        //buttons cant be pressed after game is over
        setPlayerButtonsFalse(playerOneButton, playerTwoButton);

        //startOrEndButton can create new game
        startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        startOrEndButton.setText("Start New Game");
      }

      //set score for players after check if game wis won
      getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);

    });

    playerTwoButton.addClickListener(o -> {
      playerTwo.scoredPointAgainst(playerOne);

      checkForTiebreak(playerTwo, tiebreakMessage);

      if (playerTwo.getSets() == integerField.getValue()) { //check if enough sets to win the game

        gameWon = true;

        //remove last object (bc 0 was added after game was over)
        removeLastGame(playerOne, playerTwo);

        //buttons cant be pressed after game is over
        setPlayerButtonsFalse(playerOneButton, playerTwoButton);

        //create Message that Player won
        var playerWonMessage = new Paragraph(playerTwo.getName() + " has won");
        add(playerWonMessage);

        //startOrEndButton can create new game
        startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        startOrEndButton.setText("Start New Game");
      }

      //set score for players after check if game is won
      getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);
    });

    add(playerAndSetInputFields, alignedStartButton);
    setAlignItems(Alignment.CENTER);
  }


  private static void checkForTiebreak(Player player, Paragraph paragraph){
    if (player.tiebreak){
      paragraph.setText("Tiebreak!");
    }else {
      paragraph.setText("");
    }
  }


  private static void bringNamesToSameLength(Player playerOne, Player playerTwo){
    StringBuilder toChange;

    if (playerOne.getName().length() != playerTwo.getName().length()) {

      int lengthDifference =
          Math.abs(playerOne.getName().length() - playerTwo.getName().length()) * 2 - 2;

      if (playerOne.getName().length() < playerTwo.getName().length()) {
        toChange = new StringBuilder(playerOne.getName());
        playerOne.setName(toChange.append("\u00a0".repeat(Math.max(0, lengthDifference))).toString());
      } else {
        toChange = new StringBuilder(playerTwo.getName());
        playerTwo.setName(toChange.append("\u00a0".repeat(Math.max(0, lengthDifference))).toString());
      }
    }
  }


  private static void setPlayerButtonsFalse(Button playerOneButton, Button playerTwoButton) {
    playerOneButton.setEnabled(false);
    playerTwoButton.setEnabled(false);
  }

  private static void setPlayerFieldsFalse(TextField playerOneField, TextField playerTwoField) {
    playerOneField.setEnabled(false);
    playerTwoField.setEnabled(false);
  }

  private static void getScore(Label lableOfPlayerOne, Player playerOne, Label labelOfPlayerTwo,
                               Player playerTwo) {
    lableOfPlayerOne.setText(playerOne.getScoreOfPlayer());
    labelOfPlayerTwo.setText(playerTwo.getScoreOfPlayer());
  }

  private static void removeLastGame(Player playerOne, Player playerTwo){
    playerOne.gamesStorage.remove(playerOne.gamesStorage.size() - 1);
    playerTwo.gamesStorage.remove(playerTwo.gamesStorage.size() - 1);
  }

}



