package com.example.gamestore.controller;

import com.example.gamestore.AbstractIntegrationTest;
import com.example.gamestore.dto.GenreDto;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GenreControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository genreRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDatabase() {
        genreRepository.deleteAll();
    }

    @Test
    void shouldReturnAllGenres() throws Exception {
        genreRepository.save(new GenreEntity(null, "Action"));
        genreRepository.save(new GenreEntity(null, "RPG"));

        mockMvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnGenreById() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Strategy"));

        mockMvc.perform(get("/api/v1/genres/{id}", genre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Strategy"));
    }

    @Test
    void shouldReturn404WhenGenreDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/genres/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateGenre() throws Exception {
        GenreDto dto = GenreDto.builder()
                .name("Adventure")
                .build();

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Adventure"));
    }

    @Test
    void shouldRejectInvalidGenrePayload() throws Exception {
        String invalidJson = """
        {
          "name": ""
        }
        """;

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateGenre() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Old Name"));

        String request = """
        {
          "name": "New Name"
        }
        """;

        mockMvc.perform(put("/api/v1/genres/{id}", genre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));

        assertThat(genreRepository.findById(genre.getId()).orElseThrow().getName())
                .isEqualTo("New Name");
    }

    @Test
    void shouldDeleteGenre() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Horror"));

        mockMvc.perform(delete("/api/v1/genres/{id}", genre.getId()))
                .andExpect(status().isNoContent());

        assertThat(genreRepository.findById(genre.getId())).isEmpty();
    }
}