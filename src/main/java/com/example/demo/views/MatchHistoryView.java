package com.example.demo.views;


import com.example.demo.entity.Match;
import com.example.demo.service.MatchService;
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


@Route("/matchhistory")

@PageTitle("Matchhistory")

public class MatchHistoryView extends VerticalLayout {

    Grid<Match> grid = new Grid<>(Match.class);
    TextField filteredText = new TextField();
    private MatchService matchService;

    @Autowired
    public MatchHistoryView(MatchService matchService) {
        this.matchService = matchService;
        addClassName("matchhistory-view");
        setSizeFull();
        configureGrid();
        configureFilter();

        Button loginLogoutButton = new Button();

        loginLogoutButton.setText(VaadinSession.getCurrent().getAttribute("playerOneLoggedIn") == null||
                (boolean) VaadinSession.getCurrent().getAttribute("playerOneLoggedIn") ? "Log In" : "Log Out");

        loginLogoutButton.addClickListener(event -> {
            if(loginLogoutButton.getText().equals("Log In")){
                UI.getCurrent().navigate("/login");
            }else {
                VaadinSession.getCurrent().getSession().invalidate();
            }
            VaadinSession.getCurrent().setAttribute("playerOneLoggedIn", false);
            VaadinSession.getCurrent().setAttribute("playerTwoLoggedIn", false);
        });


        HorizontalLayout header = new HorizontalLayout(loginLogoutButton);

        add(header, filteredText, grid);

        updateList();

    }

    private void configureFilter() {
        filteredText.setPlaceholder("Filter for User");
        filteredText.setClearButtonVisible(true);
        filteredText.setValueChangeMode(ValueChangeMode.LAZY);
        filteredText.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        //sets Items that either contain a filtered value or whole List
       // grid.setItems(matchService.findAllMatchesByPlayerName(filteredText.getValue()));
        grid.setItems(matchService.findAll());
    }


    private void configureGrid() {
        grid.addClassName("score-grid");
        grid.setSizeFull();
        grid.setColumns("playerOne", "playerTwo", "finalScore", "matchDate");
    }
}
