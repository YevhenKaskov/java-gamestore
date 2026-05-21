package com.example.gamestore.service.impl;

import com.example.gamestore.domain.Genre;
import com.example.gamestore.dto.GenreDto;
import com.example.gamestore.entity.GenreEntity;
import com.example.gamestore.repository.GenreRepository;
import com.example.gamestore.service.GenreService;
import com.example.gamestore.service.exception.GenreNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        List<Genre> result = new ArrayList<>();
        genreRepository.findAll().forEach(e -> result.add(toGenre(e)));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(Long id) {
        GenreEntity entity = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
        return toGenre(entity);
    }

    @Override
    @Transactional
    public Genre createGenre(GenreDto dto) {
        GenreEntity entity = new GenreEntity();
        entity.setName(dto.getName());

        try {
            return toGenre(genreRepository.save(entity));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public Genre updateGenre(GenreDto dto, Long id) {
        GenreEntity entity = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));

        entity.setName(dto.getName());

        try {
            return toGenre(genreRepository.save(entity));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

    private Genre toGenre(GenreEntity e) {
        return Genre.builder()
                .id(e.getId())
                .name(e.getName())
                .build();
    }
}