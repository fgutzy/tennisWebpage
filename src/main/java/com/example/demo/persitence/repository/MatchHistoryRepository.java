package com.example.demo.persitence.repository;

import com.example.demo.persitence.Match;
import com.example.demo.persitence.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchHistoryRepository extends JpaRepository<Match, Integer> {

    List<Match> findAllByPlayerOneOrPlayerTwo(Player playerOne, Player playerTwo);

    List<Match> findAll();
}
