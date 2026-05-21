package com.example.gamestore.controller;

import com.example.gamestore.domain.Game;
import com.example.gamestore.dto.GameRequestDto;
import com.example.gamestore.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<Game>> getGamesByGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(gameService.getGamesByGenre(genreId));
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameRequestDto dto) {
        return ResponseEntity.ok(gameService.createGame(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody GameRequestDto dto
    ) {
        return ResponseEntity.ok(gameService.updateGame(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}