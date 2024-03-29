package com.example.demo.views;

//import com.example.demo.entity.Player;

import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDataDto;
import com.example.demo.service.dto.PlayerDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/verifyResetPassword")
@Slf4j
public class VerifyResetPasswordView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    PlayerService playerService;

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        PlayerDto player = playerService.findPlayerByValidationCode(parameter);
        if (player != null) {
            VaadinSession.getCurrent().setAttribute("nameOfLoggedInUserOne", player.getName());
            VaadinSession.getCurrent().setAttribute("playerOneLoggedIn", true);
            UI.getCurrent().navigate("/login");
            }else {
            Notification.show("Invalid link");
        }
    }
}
