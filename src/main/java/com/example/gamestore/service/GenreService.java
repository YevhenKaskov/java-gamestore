package com.example.gamestore.service;

import com.example.gamestore.domain.Genre;
import com.example.gamestore.dto.GenreDto;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(Long id);

    Genre createGenre(GenreDto dto);

    Genre updateGenre(GenreDto dto, Long id);

    void deleteGenre(Long id);
}