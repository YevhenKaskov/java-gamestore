package com.example.gamestore.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {
    private Long id;
    private String title;
    private String studio;
    private String description;
    private Genre genre;
    private Double price;
}