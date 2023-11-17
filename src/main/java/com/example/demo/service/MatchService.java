package com.example.demo.service;

import com.example.demo.persitence.Match;
import com.example.demo.persitence.Player;
import com.example.demo.persitence.repository.MatchHistoryRepository;
import com.example.demo.persitence.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchHistoryRepository matchHistoryRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<Match> findAllMatchesByPlayerName(String playerName) {
        Player player = playerRepository.findPlayerByName(playerName);
        if (player == null) {
            return matchHistoryRepository.findAll();
        }
        return matchHistoryRepository.findAllByPlayerOneOrPlayerTwo(player, player);
    }

    public List<Match> findAll(){
        return matchHistoryRepository.findAll();
    }
}
