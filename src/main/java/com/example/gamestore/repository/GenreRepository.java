package com.example.gamestore.repository;

import com.example.gamestore.entity.GenreEntity;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<GenreEntity, Long> {
}