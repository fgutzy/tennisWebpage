package com.example.demo.service.dto;

import com.example.demo.entity.Player;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DtoFactory {

    @Autowired
    private ModelMapper mapper;

    public PlayerDto createDto(Player player) {
        return mapper.map(player, PlayerDto.class);
    }

    public PlayerDataDto createPlayerRegistrationDto(Player player){
        return mapper.map(player, PlayerDataDto.class);
    }
}
