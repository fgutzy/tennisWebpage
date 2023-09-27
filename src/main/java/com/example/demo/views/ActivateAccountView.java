package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.notification.Notification;


@Route("/ActivateAccount")
@Slf4j
public class ActivateAccountView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        Player player = playerRepository.findPlayerByActivationCode(parameter);
        if (player != null) {
            if (player.isAccountActivated()) {
                Notification.show("Account already activated.");
            } else {
                playerRepository.setAccountActivatedTrueByName(player.getName());
                Notification.show("Account successfully verified. Please reload Page.");
                log.info("Account from user {} was verified", player.getName());
            }
        } else {
            Notification.show("Invalid link");
        }
    }
}
