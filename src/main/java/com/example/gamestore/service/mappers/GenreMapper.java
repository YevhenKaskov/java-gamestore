package com.example.gamestore.service.mappers;

import com.example.gamestore.domain.Genre;
import com.example.gamestore.entity.GenreEntity;

public class GenreMapper {

    public static Genre toDomain(GenreEntity entity) {
        if (entity == null) return null;

        return Genre.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}