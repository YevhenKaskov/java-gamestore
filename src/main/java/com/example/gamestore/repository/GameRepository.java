package com.example.gamestore.repository;

import com.example.gamestore.entity.GameEntity;
import com.example.gamestore.entity.GenreEntity;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameEntity, Long> {

    Iterable<GameEntity> findAllByGenre(GenreEntity genre);
}