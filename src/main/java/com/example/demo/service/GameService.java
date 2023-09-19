package com.example.demo.service;


import com.example.demo.entity.Player;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  @Autowired
  PlayerService playerService;

  public void setPlayerNames(Player playerOne, TextField playerOneNameField, Player playerTwo,
                             TextField playerTwoNameField) {

    if (VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") != null){
      playerOne.setName((String) VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne"));
    } else playerOne.setName(playerOneNameField.getValue());

    if (VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserTwo") != null){
      playerTwo.setName((String) VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserTwo"));
    } else playerTwo.setName(playerTwoNameField.getValue());


    //if no input for a players name set it with Player One/Player Two
    if (playerOneNameField.getValue().isEmpty()) {
      playerOneNameField.setValue("Player One");
      playerOne.setName("Player One");
    }
    if (playerTwoNameField.getValue().isEmpty()) {
      playerTwoNameField.setValue("Player Two");
      playerTwo.setName("Player Two");
    }

    playerService.bringNamesToSameLength(playerOne, playerTwo);
  }

  public void setVariablesReadyForGame(Player playerOne, TextField playerOneNameField,
                                       Button playerOneButton, Player playerTwo,
                                       TextField playerTwoNameField, Button playerTwoButton,
                                       Button startOrEndButton, Label scoreLabelPlayerOne,
                                       Label scoreLabelPlayerTwo) {

    startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
    startOrEndButton.setText("End Game");

    getScore(scoreLabelPlayerOne, playerOne, scoreLabelPlayerTwo, playerTwo);

    //if user logged in, put his name on button
    if (VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") != null){
      playerOneButton.setText((String) VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne"));
    } else playerOneButton.setText(playerOneNameField.getValue());

    playerTwoButton.setText(playerTwoNameField.getValue());

    //initialize gamesStorage with 0 for both players
    playerOne.gamesStorage.add(0);
    playerTwo.gamesStorage.add(0);

    //deactivate buttons for name input after game is started
    setPlayerFieldsFalse(playerOneNameField, playerTwoNameField);
  }

  public void setDialog(Dialog dialog) {
    dialog.setHeaderTitle("End Game?");
    Button endGameButton = new Button("Yes, End Game", g -> UI.getCurrent().getPage().reload());
    Button cancelButton = new Button("Cancel", i -> dialog.close());
    dialog.getFooter().add(cancelButton);
    dialog.getFooter().add(endGameButton);
    dialog.open();
  }

  private static void setPlayerFieldsFalse(TextField playerOneField, TextField playerTwoField) {
    playerOneField.setEnabled(false);
    playerTwoField.setEnabled(false);
  }


  public void setValuesToEndGame(Player playerOne, Button playerOneButton, Player playerTwo,
                                 Button playerTwoButton,
                                 Button startOrEndButton,
                                 IntegerField buttonChoosingSetsNeededToWin) {

    //next Game starts with 0 as current Game
    playerService.currentGame = 0;

    //remove last object (bc 0 was added after game was over)
    playerService.removeLastGame(playerOne, playerTwo);

    //buttons cant be pressed after game is over
    setPlayerButtonsFalse(playerOneButton, playerTwoButton, buttonChoosingSetsNeededToWin);

    //startOrEndButton can create new game
    startOrEndButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
    startOrEndButton.setText("Start New Game");
  }

  private static void setPlayerButtonsFalse(Button playerOneButton, Button playerTwoButton,
                                            IntegerField buttonChoosingSetsNeededToWin) {
    playerOneButton.setEnabled(false);
    playerTwoButton.setEnabled(false);
    buttonChoosingSetsNeededToWin.setEnabled(false);
  }

  public void getScore(Label lableOfPlayerOne, Player playerOne, Label labelOfPlayerTwo,
                       Player playerTwo) {
    lableOfPlayerOne.setText(playerOne.getScoreOfPlayer());
    labelOfPlayerTwo.setText(playerTwo.getScoreOfPlayer());
  }

  public void setNameFields(TextField playerOneNameField, TextField playerTwoNameField) {

    String nameOfLoggedInUserOne = (String) VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne");
    String nameOfLoggedInUserTwo = (String) VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserTwo");


    if (VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") != null) {
      playerOneNameField.setValue(nameOfLoggedInUserOne);
      playerOneNameField.setEnabled(false);
    }

    if (VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserTwo") != null){
      playerTwoNameField.setValue(nameOfLoggedInUserTwo);
      playerTwoNameField.setEnabled(false);
    }
  }
}
