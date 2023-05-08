package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="matchhistory")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "playerOne_username")
    @Getter
    @Setter
    Player playerOne;

    @ManyToOne
    @JoinColumn(name = "playerTwo_username")
    @Getter
    @Setter
    Player playerTwo;

    private Date matchDate;

    @Getter
    @Setter
    private String finalScore;

    public Match(String finalScore) {
        this.finalScore = finalScore;
    }
}
