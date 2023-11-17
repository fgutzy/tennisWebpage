package com.example.demo.persitence;

import com.example.demo.service.dto.PlayerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Board {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    @ManyToOne
    Player ownerOfBoard;
    String text;
    int level;

    public Board(Player ownerOfBoard, String text, int level) {
        this.ownerOfBoard = ownerOfBoard;
        this.text = text;
        this.level = level;
    }
}
