package com.example.gamestore.controller;

import com.example.gamestore.AbstractIntegrationTest;
import com.example.gamestore.entity.GameEntity;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GameRepository;
import com.example.gamestore.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GameControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void cleanDatabase() {
        gameRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    void shouldReturnAllGames() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Action"));

        gameRepository.save(new GameEntity(null, "GTA V", "Rockstar", "Open world", genre, 29.99));
        gameRepository.save(new GameEntity(null, "Cyberpunk", "CDPR", "RPG", genre, 19.99));

        mockMvc.perform(get("/api/v1/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnGameById() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "RPG"));

        GameEntity game = gameRepository.save(
                new GameEntity(null, "Witcher 3", "CDPR", "RPG game", genre, 39.99)
        );

        mockMvc.perform(get("/api/v1/games/{id}", game.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Witcher 3"))
                .andExpect(jsonPath("$.genre.name").value("RPG"));
    }

    @Test
    void shouldReturn404WhenGameDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/games/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void shouldReturnGamesByGenre() throws Exception {
        GenreEntity action = genreRepository.save(new GenreEntity(null, "Action"));
        GenreEntity rpg = genreRepository.save(new GenreEntity(null, "RPG"));

        gameRepository.save(new GameEntity(null, "GTA V", "Rockstar", "Open world", action, 29.99));
        gameRepository.save(new GameEntity(null, "Witcher 3", "CDPR", "RPG game", rpg, 39.99));

        mockMvc.perform(get("/api/v1/games/genre/{genreId}", action.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("GTA V"));
    }

    @Test
    void shouldCreateGame() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Strategy"));

        String request = """
        {
          "title": "Starcraft",
          "studio": "Blizzard",
          "description": "Real-time strategy game",
          "genreId": %d,
          "price": 15.0
        }
        """.formatted(genre.getId());

        mockMvc.perform(post("/api/v1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Starcraft"));
    }

    @Test
    void shouldRejectInvalidGamePayload() throws Exception {
        String invalidJson = """
        {
          "title": "",
          "studio": "A",
          "description": "bad",
          "genreId": -1,
          "price": -10.0
        }
        """;

        mockMvc.perform(post("/api/v1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateGame() throws Exception {
        GenreEntity oldGenre = genreRepository.save(new GenreEntity(null, "Action"));
        GenreEntity newGenre = genreRepository.save(new GenreEntity(null, "RPG"));

        GameEntity game = gameRepository.save(
                new GameEntity(null, "Old Title", "Old Studio", "Old Desc", oldGenre, 10.0)
        );

        String request = """
        {
          "title": "New Title",
          "studio": "New Studio",
          "description": "New Desc",
          "genreId": %d,
          "price": 99.99
        }
        """.formatted(newGenre.getId());

        mockMvc.perform(put("/api/v1/games/{id}", game.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.genre.name").value("RPG"));

        assertThat(gameRepository.findById(game.getId()).orElseThrow().getTitle())
                .isEqualTo("New Title");
    }

    @Test
    void shouldDeleteGame() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "FPS"));

        GameEntity game = gameRepository.save(
                new GameEntity(null, "DOOM", "id Software", "Shooter", genre, 9.99)
        );

        mockMvc.perform(delete("/api/v1/games/{id}", game.getId()))
                .andExpect(status().isNoContent());

        assertThat(gameRepository.findById(game.getId())).isEmpty();
    }
}