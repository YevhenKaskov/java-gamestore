package com.example.gamestore.service.impl;

import com.example.gamestore.domain.Game;
import com.example.gamestore.dto.GameRequestDto;
import com.example.gamestore.entity.GameEntity;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GameRepository;
import com.example.gamestore.repository.GenreRepository;
import com.example.gamestore.service.GameService;
import com.example.gamestore.service.exception.GameNotFoundException;
import com.example.gamestore.service.exception.GenreNotFoundException;
import com.example.gamestore.service.mappers.GameMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final GameMapper gameMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        return gameMapper.toGameList(gameRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Game getGameById(Long id) {
        return gameMapper.toGame(
                gameRepository.findById(id)
                        .orElseThrow(() -> new GameNotFoundException(id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getGamesByGenre(Long genreId) {
        GenreEntity genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        return gameMapper.toGameList(
                gameRepository.findAllByGenre(genre)
        );
    }

    @Override
    @Transactional
    public Game createGame(GameRequestDto dto) {
        GenreEntity genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new GenreNotFoundException(dto.getGenreId()));

        GameEntity entity = gameMapper.toGameEntity(dto);
        entity.setGenre(genre);

        return gameMapper.toGame(gameRepository.save(entity));
    }

    @Override
    @Transactional
    public Game updateGame(GameRequestDto dto, Long id) {
        GameEntity entity = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        GenreEntity genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new GenreNotFoundException(dto.getGenreId()));

        entity.setTitle(dto.getTitle());
        entity.setStudio(dto.getStudio());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setGenre(genre);

        return gameMapper.toGame(gameRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}