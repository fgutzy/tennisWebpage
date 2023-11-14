package com.example.demo.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;
@Data
public class PlayerDto {

    //private UUID id;

    private String name;

    public ArrayList<Integer> gamesStorage = new ArrayList<>();

    private int gamesWon = 0;

    private int gamesLost = 0;

    private int gamesPlayed = 0;

    //private double winningPercentage = 0.0;

    private int sets = 0;

    private int games;

    private int points = 0;

    private boolean hasAdvantage;

    private boolean tiebreak = false;

    private String printADOrNot = "";

    private String email;

    private boolean accountActivated;

    public PlayerDto() {
        this.name = "";
    }

    public String getScoreOfPlayer() {
        return this.getName() + " " + this.getSets() + " " +
                this.gamesStorage + " " +
                this.getPoints() +
                this.printADOrNot;
    }
}

