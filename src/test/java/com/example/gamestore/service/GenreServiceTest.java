package com.example.gamestore.service;

import com.example.gamestore.domain.Genre;
import com.example.gamestore.dto.GenreDto;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GenreRepository;
import com.example.gamestore.service.exception.GenreNotFoundException;
import com.example.gamestore.service.impl.GenreServiceImpl;
import com.example.gamestore.service.mappers.GenreMapper;
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

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    private GenreEntity genreEntity;
    private Genre genre;
    private GenreDto dto;

    @BeforeEach
    void setUp() {
        genreEntity = new GenreEntity(1L, "Action");

        genre = Genre.builder()
                .id(1L)
                .name("Action")
                .build();

        dto = GenreDto.builder()
                .name("Action")
                .build();
    }

    @Test
    void shouldGetAllGenres() {
        when(genreRepository.findAll()).thenReturn(List.of(genreEntity));
        when(genreMapper.toGenreList(List.of(genreEntity))).thenReturn(List.of(genre));

        List<Genre> result = genreService.getAllGenres();

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetGenreById() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);

        Genre result = genreService.getGenreById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenGenreNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class,
                () -> genreService.getGenreById(1L));
    }

    @Test
    void shouldCreateGenre() {
        when(genreRepository.save(any())).thenReturn(genreEntity);
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);

        Genre result = genreService.createGenre(dto);

        assertNotNull(result);
        assertEquals("Action", result.getName());
    }

    @Test
    void shouldUpdateGenre() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreRepository.save(genreEntity)).thenReturn(genreEntity);
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);

        Genre result = genreService.updateGenre(dto, 1L);

        assertNotNull(result);
        verify(genreRepository).save(genreEntity);
    }

    @Test
    void shouldDeleteGenre() {
        genreService.deleteGenre(1L);

        verify(genreRepository).deleteById(1L);
    }
}