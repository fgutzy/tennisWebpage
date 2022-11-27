package com.example.demo;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import javax.validation.constraints.NotNull;

@Route("")
public class Game extends VerticalLayout {

  int firstClickOnStartOrEndButton = 1;
  int totalSetsNeededToWin;
  boolean gameWon = false;

  public Game() {

    Label scoreOfPlayerOne = new Label();
    Label scoreOfPlayerTwo = new Label();

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


    var playerAndSetInputFields = new HorizontalLayout(playerOneNameField, playerTwoNameField, integerField);
    var alignedStartButton = new HorizontalLayout(startOrEndButton); //button in new line

    playerAndSetInputFields.setDefaultVerticalComponentAlignment(Alignment.AUTO);
    alignedStartButton.setVerticalComponentAlignment(Alignment.CENTER);


    startOrEndButton.addClickListener(e -> {

      playerOne.setName(playerOneNameField.getValue());
      playerTwo.setName(playerTwoNameField.getValue());

      if (playerOne.getName().length() != playerTwo.getName().length()){
        String toChange;
        if (playerOne.getName().length() < playerTwo.getName().length()){
          toChange = playerOne.getName();
        } else toChange = playerTwo.getName();

        int lengthDifference = Math.abs(playerOne.getName().length()-playerTwo.getName().length())*2-2;
        for (int i = 0; i < lengthDifference; i++){
          toChange += "\u00a0";
        }

        if (playerOne.getName().length() < playerTwo.getName().length()){
          playerOne.setName(toChange);
        } else playerTwo.setName(toChange);
      }


      playerOneNameField.setEnabled(false);
      playerTwoNameField.setEnabled(false);

      if (firstClickOnStartOrEndButton == 1) {
        startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        startOrEndButton.setText("End Game");

        scoreOfPlayerOne.setText(displayScoreOfPlayerOne(playerOne));
        scoreOfPlayerTwo.setText(displayScoreOfPlayerTwo(playerTwo));
        var displayScore = new VerticalLayout(scoreOfPlayerOne, scoreOfPlayerTwo);
        displayScore.setAlignItems(Alignment.CENTER);
        add(displayScore);

        // by creating buttons on top they can be accessed for the eventListener of playerButton as well
        playerOneButton.setText(playerOneNameField.getValue());
        playerTwoButton.setText(playerTwoNameField.getValue());

        var playOngoing = new HorizontalLayout(playerOneButton, playerTwoButton);
        add(playOngoing);

        firstClickOnStartOrEndButton++;

      } else {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("End Game?");

        Button endGameButton = new Button("Yes, End Game", g->  UI.getCurrent().getPage().reload());
        Button cancelButton = new Button("Cancel", i -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(endGameButton);
        dialog.open();

        add(dialog);
      }
    });

    playerOneButton.addClickListener(o -> {
      playerOne.scoredPointAgainst(playerTwo);
      scoreOfPlayerOne.setText(displayScoreOfPlayerOne(playerOne));
      scoreOfPlayerTwo.setText(displayScoreOfPlayerTwo(playerTwo));
      if (playerOne.getSets() == integerField.getValue()){
        gameWon = true;
        playerOne.gamesStorage.remove(playerOne.gamesStorage.size()-1);
        playerTwo.gamesStorage.remove(playerTwo.gamesStorage.size()-1);
        scoreOfPlayerOne.setText(displayScoreOfPlayerOne(playerOne));
        scoreOfPlayerTwo.setText(displayScoreOfPlayerTwo(playerTwo));
        var playerWonMessage = new Paragraph(playerOne.getName() + " has won");
        add(playerWonMessage);
        playerOneButton.setEnabled(false);
        playerTwoButton.setEnabled(false);
        startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        startOrEndButton.setText("Start New Game");
      }

    });

    playerTwoButton.addClickListener(o -> {
      playerTwo.scoredPointAgainst(playerOne);
      scoreOfPlayerOne.setText(displayScoreOfPlayerOne(playerOne));
      scoreOfPlayerTwo.setText(displayScoreOfPlayerTwo(playerTwo));

    });

    add(playerAndSetInputFields, alignedStartButton);
    setAlignItems(Alignment.CENTER);
  }


  private String displayScoreOfPlayerOne(Player playerOne) {
    return  playerOne.getName() + " " + playerOne.getSets() + " " +
        playerOne.gamesStorage + " " +
        playerOne.getPoints() +
        playerOne.printADOrNot;

  }

  private String displayScoreOfPlayerTwo(Player playerTwo) {
    return playerTwo.getName() + " " + playerTwo.getSets() + " " +
        playerTwo.gamesStorage + " " +
        playerTwo.getPoints() +
        playerTwo.printADOrNot;
  }
}



