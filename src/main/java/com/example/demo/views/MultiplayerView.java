package com.example.demo.views;

import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/multiplayer")
@PageTitle("Multiplayer")
public class MultiplayerView extends VerticalLayout {

    @Autowired
    PlayerRepository playerRepository;


    public MultiplayerView() {

        HorizontalLayout layout = new HorizontalLayout();

        VerticalLayout leftLayout = new VerticalLayout();
        VerticalLayout rightLayout = new VerticalLayout();

        var description = new H1("Log in both users to play against each other");

        var usernameOne = new TextField("Username Player One");
        var passwordOne = new PasswordField("Password Player One");

        var usernameTwo = new TextField("Username Player Two");
        var passwordTwo = new PasswordField("Password Player Two");

        leftLayout.add(
                usernameOne,
                passwordOne
        );

        rightLayout.add(
                usernameTwo,
                passwordTwo
        );

        var loginButton = new Button("Login and start", event -> {

            boolean playerOneLoggedIn = checkCredentials(usernameOne, passwordOne);
            boolean playerTwoLoggedIn = checkCredentials(usernameTwo, passwordTwo);

            if (playerOneLoggedIn && playerTwoLoggedIn) {
                VaadinSession.getCurrent().setAttribute("playerOneLoggedIn", true);
                VaadinSession.getCurrent().setAttribute("playerTwoLoggedIn", true);
                VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserOne", usernameOne.getValue());
                VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserTwo", usernameTwo.getValue());
                UI.getCurrent().navigate("/game");
            } else {
                if (!playerOneLoggedIn) {
                    Notification.show("Wrong credentials for Player One");
                }
                if (!playerTwoLoggedIn) {
                    Notification.show("Wrong credentials for Player Two");
                }
            }
        });

        add(description);
        layout.add(leftLayout, rightLayout);
        add(layout, loginButton);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    private boolean checkCredentials(TextField username, PasswordField password) {
        if (playerRepository.findPlayerByNameAndPassword(username.getValue(), password.getValue()) != null) {
            username.setEnabled(false);
            password.setEnabled(false);
            return true;
        }
        return false;
    }
}

