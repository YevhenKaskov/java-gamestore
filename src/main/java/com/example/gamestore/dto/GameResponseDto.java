package com.example.gamestore.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GameResponseDto {
    Long id;
    String title;
    String studio;
    String description;
    GenreDto genre;
    Double price;
}