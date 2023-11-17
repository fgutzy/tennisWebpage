package com.example.demo.views;

import com.example.demo.persitence.repository.BoardRepository;
import com.example.demo.persitence.repository.PlayerRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Route("/createBoard")
@Slf4j
public class BoardCreationView extends VerticalLayout {

    PlayerRepository playerRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    public BoardCreationView(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;

        TextArea text = new TextArea("Enter your text");
        text.setHeight("250px");
        text.setWidth("400px");

        IntegerField level = new IntegerField("Level (Int. point system)");
        level.setHasControls(true);
        level.setValue(3);
        level.setMin(1);
        level.setMax(10);

        Button saveButton = new Button("Save", event -> {
            if (VaadinSession.getCurrent().getAttribute("nameOfLoggedInUserOne") != null) {
                boardService.createBoard(String.valueOf(VaadinSession.getCurrent().getAttribute("uuid")),
                        text.getValue(), level.getValue());
                text.setValue("");
                level.setValue(3);
                Notification.show("Board created");
                log.info("Board created");
            }else {
                Notification.show("You have to login to post");
            }
        });

        add(text, level, saveButton);

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }
}
