package com.example.demo.service.dto;

import lombok.Data;

@Data
public class PlayerDataDto {
    String password;
    String validationCode;
    String email;
    private boolean accountActivated;
}
