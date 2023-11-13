package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name ="matchhistory")
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    private String matchDate;

    @Getter
    @Setter
    private String finalScore;

    @Getter
    @Setter
    private String playerOne;

    @Getter
    @Setter
    private String playerTwo;

    public Match(String playerOne, String playerTwo, String finalScore) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.finalScore = finalScore;
        this.matchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
    }
}
