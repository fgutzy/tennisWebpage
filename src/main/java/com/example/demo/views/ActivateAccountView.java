package com.example.demo.views;

import com.example.demo.repository.PlayerRepository;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("EmailVerification/ActivateAccount")
public class ActivateAccountView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        System.out.println(parameter);
    }
}
