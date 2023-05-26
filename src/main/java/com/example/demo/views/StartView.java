package com.example.demo.views;

import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.LogInService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/")
@PageTitle("start")
public class StartView extends VerticalLayout {

    public StartView() {

        add(
                new H1("Log in to save your data, or click Quickstart"),
                new RouterLink("Login", LoginView.class),
                new RouterLink("Register Account", RegisterView.class),
                new RouterLink("Quickstart", GameView.class),
                new RouterLink("Leaderboard", LeaderboardView.class),
                new RouterLink("Matchhistory", MatchHistoryView.class)
        );

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}