package com.example.demo.views;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.EmailService;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

@Route("EmailVerification/ActivateAccount")
public class ActivateAccountView {

    @Autowired
    PlayerRepository playerRepository;

    EmailService emailService;
    @Autowired
    public ActivateAccountView(EmailService emailService) {
        this.emailService = emailService;
/*
        HttpServletRequest httpServletRequest;

        String urlKey = httpServletRequest.getRequestURL().toString();

        if (playerRepository.findByActivationCode(urlKey)){
            setAccountActivated(true);
        }

 */
    }

    private String getKey(HttpServletRequest request) {
        return request.getParameter("verificationKey")!= null
                ? "verificationKey = request.getParameter(\"verificationKey\")":"null";
    }
}
