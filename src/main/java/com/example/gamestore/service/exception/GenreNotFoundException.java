package com.example.gamestore.service.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(Long id) {
        super("Genre with id " + id + " was not found.");
    }
}