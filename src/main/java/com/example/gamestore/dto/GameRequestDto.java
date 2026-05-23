package com.example.gamestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;

    @NotBlank(message = "Studio is required")
    @Size(min = 2, max = 50, message = "Studio must be between 2 and 50 characters")
    private String studio;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotNull(message = "Genre is required")
    @Positive(message = "Genre id must be positive")
    private Long genreId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
}