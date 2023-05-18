package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="matchhistory")
@NoArgsConstructor
public class Match {

    @Id
    /*
    @SequenceGenerator(name = "match_sequence",
            sequenceName = "match_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "match_sequence"
    )

     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    private Date matchDate;

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
    }

    public Match(String playerOne, String finalScore) {
        this.playerOne = playerOne;
        this.finalScore = finalScore;
    }

}
