package com.example.demo;


import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.GameService;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import java.awt.*;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;


@Route("s")
public class ScoreView extends VerticalLayout {

  Grid<Player> grid = new Grid<>(Player.class);
  TextField filteredText = new TextField();

  private PlayerService playerService;

  @Autowired
  PlayerRepository playerRepository;

  @Autowired
  GameService gameService;


  public ScoreView(PlayerService playerService) {
    this.playerService = playerService;
    addClassName("Score-view");
    setSizeFull();
    configureGrid();
    configureFilter();

    add(filteredText, grid);

    updateList();

  }

  private void configureFilter() {
    filteredText.setPlaceholder("Filter for Player");
    filteredText.setClearButtonVisible(true);
    filteredText.setValueChangeMode(ValueChangeMode.LAZY);
    filteredText.addValueChangeListener(e -> updateList());
  }


  private void updateList() {

    //sets Items that either contain a filtered value or whole List
    grid.setItems(playerService.findAll(filteredText.getValue()));
  }


  private void configureGrid() {
    grid.addClassName("score-grid");
    grid.setSizeFull();
    grid.setColumns("name", "gamesWon", "gamesLost");
    grid.addColumn(Player::getGamesPlayed).setHeader("gamesPlayed");
  }

}
