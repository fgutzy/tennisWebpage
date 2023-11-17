package com.example.demo.views;


import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDto;
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
import org.springframework.beans.factory.annotation.Autowired;


@Route("/leaderboard")
@PageTitle("Leaderboard")
public class LeaderboardView extends VerticalLayout {

  Grid<PlayerDto> grid = new Grid<>(PlayerDto.class);
  TextField filteredText = new TextField();


  PlayerService playerService;
  @Autowired
  public LeaderboardView(PlayerService playerService) {
    this.playerService = playerService;
    addClassName("leaderboard-view");
    setSizeFull();
    configureGrid();
    configureFilter();


    Button loginLogoutButton = new Button();

    loginLogoutButton.setText(VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") == null
            ? "Log In" : "Log Out");

    loginLogoutButton.addClickListener(event -> {
        if(loginLogoutButton.getText().equals("Log In")){
          UI.getCurrent().navigate("/login");
        }else {
          VaadinSession.getCurrent().getSession().invalidate();
        }
        VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserOne", null);
        VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserTwo", null);
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
    grid.addColumn(PlayerDto::getName).setHeader("Name").setSortable(true);
    grid.addColumn(PlayerDto::getGamesWon).setHeader("Wins").setSortable(true);
    grid.addColumn(PlayerDto::getGamesLost).setHeader("Losses").setSortable(true);
    grid.addColumn(PlayerDto::getGamesPlayed).setHeader("Games Played").setSortable(true);
    //grid.addColumn(Player::getWinningPercentage).setHeader("Winning Percentage").setSortable(true);
  }
}
