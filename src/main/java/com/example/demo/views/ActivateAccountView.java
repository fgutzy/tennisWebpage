package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import lombok.extern.slf4j.Slf4j;
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
        if (player != null){
            playerRepository.setAccountActivatedTrueByName(player.getName());
            Notification.show("Account successfully verified");
            log.info("Account from user {} was verified", player.getName());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UI.getCurrent().navigate("/login");
        }
    }
}
