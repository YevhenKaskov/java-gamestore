package com.example.gamestore.service.mappers;

import com.example.gamestore.domain.Game;
import com.example.gamestore.entity.GameEntity;

public class GameMapper {

    public static Game toDomain(GameEntity entity) {
        if (entity == null) return null;

        return Game.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .studio(entity.getStudio())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }

    public static GameEntity toEntity(Game domain) {
        if (domain == null) return null;

        GameEntity entity = new GameEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setStudio(domain.getStudio());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        return entity;
    }
}