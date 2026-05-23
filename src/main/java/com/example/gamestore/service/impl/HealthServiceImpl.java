package com.example.gamestore.service.impl;

import com.example.gamestore.repository.GameRepository;
import com.example.gamestore.service.HealthService;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    private final GameRepository gameRepository;

    public HealthServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isHealthy() {
        try {
            gameRepository.count();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
