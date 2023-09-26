package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/ActivateAccount")
public class ActivateAccountView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        Player player = playerRepository.findPlayerByActivationCode(parameter);
        if (player != null){
            playerRepository.setAccountActivatedTrueByName(player.getName());
            UI.getCurrent().navigate("/login");
        }
    }
}
