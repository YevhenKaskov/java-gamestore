package com.example.gamestore.service.impl;

import com.example.gamestore.domain.Genre;
import com.example.gamestore.dto.GenreDto;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GenreRepository;
import com.example.gamestore.service.GenreService;
import com.example.gamestore.service.exception.GenreNotFoundException;
import com.example.gamestore.service.mappers.GenreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreMapper.toGenreList(genreRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(Long id) {
        return genreMapper.toGenre(
                genreRepository.findById(id)
                        .orElseThrow(() -> new GenreNotFoundException(id))
        );
    }

    @Override
    @Transactional
    public Genre createGenre(GenreDto dto) {
        GenreEntity entity = new GenreEntity();
        entity.setName(dto.getName());

        return genreMapper.toGenre(
                genreRepository.save(entity)
        );
    }

    @Override
    @Transactional
    public Genre updateGenre(GenreDto dto, Long id) {
        GenreEntity entity = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));

        entity.setName(dto.getName());

        return genreMapper.toGenre(
                genreRepository.save(entity)
        );
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}