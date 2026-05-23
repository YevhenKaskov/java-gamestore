package com.example.gamestore.service.mappers;

import com.example.gamestore.domain.Genre;
import com.example.gamestore.dto.GenreDto;
import com.example.gamestore.entity.GenreEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper {
    GenreEntity toGenreEntity(Genre genre);

    Genre toGenre(GenreEntity entity);

    List<Genre> toGenreList(Iterable<GenreEntity> entities);

    @Mapping(target = "id", ignore = true)
    Genre toGenre(GenreDto dto);

    GenreDto toGenreDto(Genre genre);
}