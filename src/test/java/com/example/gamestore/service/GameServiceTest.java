package com.example.gamestore.service;

import com.example.gamestore.domain.Game;
import com.example.gamestore.dto.GameRequestDto;
import com.example.gamestore.entity.GameEntity;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GameRepository;
import com.example.gamestore.repository.GenreRepository;
import com.example.gamestore.service.exception.GameNotFoundException;
import com.example.gamestore.service.exception.GenreNotFoundException;
import com.example.gamestore.service.impl.GameServiceImpl;
import com.example.gamestore.service.mappers.GameMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameServiceImpl gameService;

    private GameEntity gameEntity;
    private GenreEntity genreEntity;
    private Game game;
    private GameRequestDto dto;

    @BeforeEach
    void setUp() {
        genreEntity = new GenreEntity(1L, "Action");

        gameEntity = new GameEntity(
                1L,
                "GTA V",
                "Rockstar",
                "Open world",
                genreEntity,
                29.99
        );

        dto = GameRequestDto.builder()
                .title("GTA V")
                .studio("Rockstar")
                .description("Open world")
                .genreId(1L)
                .price(29.99)
                .build();

        game = Game.builder()
                .id(1L)
                .title("GTA V")
                .studio("Rockstar")
                .description("Open world")
                .price(29.99)
                .build();
    }

    @Test
    void shouldGetAllGames() {
        when(gameRepository.findAll()).thenReturn(List.of(gameEntity));
        when(gameMapper.toGameList(List.of(gameEntity))).thenReturn(List.of(game));

        List<Game> result = gameService.getAllGames();

        assertEquals(1, result.size());
        assertEquals("GTA V", result.getFirst().getTitle());
    }

    @Test
    void shouldGetGameById() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toGame(gameEntity)).thenReturn(game);

        Game result = gameService.getGameById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenGameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> gameService.getGameById(1L));
    }

    @Test
    void shouldGetGamesByGenre() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(gameRepository.findAllByGenre(genreEntity)).thenReturn(List.of(gameEntity));
        when(gameMapper.toGameList(List.of(gameEntity))).thenReturn(List.of(game));

        List<Game> result = gameService.getGamesByGenre(1L);

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenGenreNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class,
                () -> gameService.getGamesByGenre(1L));
    }

    @Test
    void shouldCreateGame() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(gameMapper.toGameEntity(any(GameRequestDto.class))).thenReturn(gameEntity);
        when(gameRepository.save(gameEntity)).thenReturn(gameEntity);
        when(gameMapper.toGame(gameEntity)).thenReturn(game);

        Game result = gameService.createGame(dto);

        assertNotNull(result);
        assertEquals("GTA V", result.getTitle());
    }

    @Test
    void shouldUpdateGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameEntity));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(gameRepository.save(gameEntity)).thenReturn(gameEntity);
        when(gameMapper.toGame(gameEntity)).thenReturn(game);

        Game result = gameService.updateGame(dto, 1L);

        assertNotNull(result);
        verify(gameRepository).save(gameEntity);
    }

    @Test
    void shouldDeleteGame() {
        gameService.deleteGame(1L);

        verify(gameRepository).deleteById(1L);
    }
}