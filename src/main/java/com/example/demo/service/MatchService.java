package com.example.demo.service;

import com.example.demo.entity.Match;
import com.example.demo.entity.Player;
import com.example.demo.repository.MatchHistoryRepository;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
}
