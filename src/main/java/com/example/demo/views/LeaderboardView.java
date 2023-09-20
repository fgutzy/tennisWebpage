package com.example.demo.views;


import com.example.demo.entity.Player;
import com.example.demo.service.PlayerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route("/leaderboard")
@PageTitle("Leaderboard")

public class LeaderboardView extends VerticalLayout {

  Grid<Player> grid = new Grid<>(Player.class);
  TextField filteredText = new TextField();

  private PlayerService playerService;

  public LeaderboardView(PlayerService playerService) {
    this.playerService = playerService;
    addClassName("leaderboard-view");
    setSizeFull();
    configureGrid();
    configureFilter();



    Button loginLogoutButton = new Button();

    loginLogoutButton.setText((boolean) VaadinSession.getCurrent().getAttribute("playerOneLoggedIn")
            ? "Log In" : "Log out");

    loginLogoutButton.addClickListener(event -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      UI.getCurrent().navigate("/login");
      VaadinSession.getCurrent().setAttribute("playerOneLoggedIn", false);
      VaadinSession.getCurrent().setAttribute("playerTwoLoggedIn", false);
    });


    HorizontalLayout header = new HorizontalLayout(loginLogoutButton);

    add(header, filteredText, grid);

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
    grid.removeAllColumns();
    grid.addColumn(Player::getName).setHeader("Name").setSortable(true);
    grid.addColumn(Player::getGamesWon).setHeader("Wins").setSortable(true);
    grid.addColumn(Player::getGamesLost).setHeader("Losses").setSortable(true);
    grid.addColumn(Player::getGamesPlayed).setHeader("Games Played").setSortable(true);
    //grid.addColumn(Player::getWinningPercentage).setHeader("Winning Percentage").setSortable(true);
  }
}
