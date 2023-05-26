package com.example.demo.entity;

import java.util.ArrayList;
import javax.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name = "tbl_player")
@NoArgsConstructor
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String password;

  @Column(name = "Name_of_Player")
  private String name;

  @Transient
  public ArrayList<Integer> gamesStorage = new ArrayList<>();

  private int gamesWon = 0;

  private int gamesLost = 0;

  private int gamesPlayed = 0;

  //private double winningPercentage = 0.0;

  @Transient
  private int sets = 0;

  @Transient
  private int games;

  @Transient
  private int points = 0;

  @Transient
  private boolean hasAdvantage;

  @Transient
  private boolean tiebreak = false;

  @Transient
  private String printADOrNot = "";

  public Player(String name) {
    this.name = name;
  }

  public Player(String name, String password){
    this.name = name;
    this.password = password;
  }


  public String getScoreOfPlayer() {
    return this.getName() + " " + this.getSets() + " " +
        this.gamesStorage + " " +
        this.getPoints() +
        this.printADOrNot;
  }
}
