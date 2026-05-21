package com.example.gamestore.service.impl;

import com.example.gamestore.domain.Game;
import com.example.gamestore.domain.Genre;
import com.example.gamestore.dto.GameRequestDto;
import com.example.gamestore.entity.GameEntity;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GameRepository;
import com.example.gamestore.repository.GenreRepository;
import com.example.gamestore.service.GameService;
import com.example.gamestore.service.exception.GameNotFoundException;
import com.example.gamestore.service.exception.GenreNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;

    public GameServiceImpl(GameRepository gameRepository,
                           GenreRepository genreRepository) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        List<Game> result = new ArrayList<>();
        gameRepository.findAll().forEach(e -> result.add(toGame(e)));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Game getGameById(Long id) {
        GameEntity entity = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        return toGame(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getGamesByGenre(Long genreId) {
        GenreEntity genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        List<Game> result = new ArrayList<>();
        gameRepository.findAllByGenre(genre).forEach(e -> result.add(toGame(e)));
        return result;
    }

    @Override
    @Transactional
    public Game createGame(GameRequestDto dto) {
        GenreEntity genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new GenreNotFoundException(dto.getGenreId()));

        GameEntity entity = new GameEntity();
        return saveGame(dto, entity, genre);
    }

    @Override
    @Transactional
    public Game updateGame(GameRequestDto dto, Long id) {
        GameEntity entity = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        GenreEntity genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new GenreNotFoundException(dto.getGenreId()));

        return saveGame(dto, entity, genre);
    }

    private Game saveGame(GameRequestDto dto, GameEntity entity, GenreEntity genre) {
        entity.setTitle(dto.getTitle());
        entity.setStudio(dto.getStudio());
        entity.setDescription(dto.getDescription());
        entity.setGenre(genre);
        entity.setPrice(dto.getPrice());

        try {
            return toGame(gameRepository.save(entity));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    private Game toGame(GameEntity e) {
        return Game.builder()
                .id(e.getId())
                .title(e.getTitle())
                .studio(e.getStudio())
                .description(e.getDescription())
                .price(e.getPrice())
                .genre(
                        Genre.builder()
                                .id(e.getGenre().getId())
                                .name(e.getGenre().getName())
                                .build()
                )
                .build();
    }
}