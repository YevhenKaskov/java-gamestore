package com.example.gamestore.service.mappers;

import com.example.gamestore.domain.Game;
import com.example.gamestore.dto.GameRequestDto;
import com.example.gamestore.entity.GameEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface GameMapper {
    Game toGame(GameEntity entity);

    List<Game> toGameList(Iterable<GameEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "genre", ignore = true)
    GameEntity toGameEntity(GameRequestDto dto);
}