package com.example.gamestore.service;

import com.example.gamestore.domain.Game;
import com.example.gamestore.dto.GameRequestDto;

import java.util.List;

public interface GameService {
    List<Game> getAllGames();

    Game getGameById(Long id);

    List<Game> getGamesByGenre(Long genreId);

    Game createGame(GameRequestDto dto);

    Game updateGame(GameRequestDto dto, Long id);

    void deleteGame(Long id);
}