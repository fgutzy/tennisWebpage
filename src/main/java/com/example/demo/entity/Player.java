package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Set;
import javax.persistence.*;
import lombok.*;


@Entity
//@Data
@Table(name = "tbl_player")
@NoArgsConstructor
public class Player {

  @Id
  @SequenceGenerator(name = "player_sequence",
          sequenceName = "player_sequence",
          allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "player_sequence"
  )
  int id;

  @Getter
  @Setter
  String password;

  @Getter
  @Setter
  @Column(name = "Name_of_Player")
  String name;

  public ArrayList<Integer> gamesStorage = new ArrayList<>();

  @Getter
  @Setter
  int gamesWon = 0;

  @Getter
  @Setter
  int gamesLost = 0;

  @Getter
  @Setter
  int gamesPlayed = 0;

  @Getter
  @Setter
  int sets = 0;

  @Getter
  @Setter
  int games;

  @Getter
  @Setter
  int points = 0;

  @OneToMany(mappedBy = "playerOne")
  private Set<Match> playerOneMatches;

  @OneToMany(mappedBy = "playerTwo")
  private Set<Match> playerTwoMatches;


  public boolean hasAdvantage;
  public boolean tiebreak = false;

  public String printADOrNot = "";


  public Player(String name) {
    this.name = name;
  }

  public Player( int id, String name, String password){
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public Player( String name, String password){
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
