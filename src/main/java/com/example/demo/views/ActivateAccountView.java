package com.example.demo.views;

import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.PlayerService;
import com.example.demo.service.dto.PlayerDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/ActivateAccount")
@Slf4j
public class ActivateAccountView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerService playerService;

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        PlayerDto player = playerService.findPlayerByValidationCode(parameter);
        if (player != null) {
            if (player.isAccountActivated()) {
                Notification.show("Account already activated.");
            } else {
                playerRepository.updateAccountActivatedTrueByName(player.getName());
                Notification.show("Account successfully verified. Please reload Page.");
                UI.getCurrent().navigate("/login");
                log.info("Account from user {} was verified", player.getName());
            }
        } else {
            Notification.show("Invalid link");
        }
    }
}
