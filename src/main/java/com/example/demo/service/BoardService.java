package com.example.demo.service;

import com.example.demo.persitence.Board;
import com.example.demo.persitence.Player;
import com.example.demo.persitence.repository.BoardRepository;
import com.example.demo.persitence.repository.PlayerRepository;
import com.example.demo.service.dto.DtoFactory;
import com.example.demo.service.dto.PlayerDto;
import com.vaadin.flow.server.VaadinSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BoardService {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PlayerRepository playerRepository;

    public void createBoard(String uuid, String text, int level){
        log.info("created Board for user with uuid " + uuid);
        Player owner = playerRepository.findPlayerById(UUID.fromString(uuid));
        boardRepository.save(new Board(owner, text, level));
    }
}
