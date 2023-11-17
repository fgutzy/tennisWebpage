package com.example.demo.service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerDataDto {

    UUID id;
    String password;
    String validationCode;
    String email;
    private boolean accountActivated;
}
