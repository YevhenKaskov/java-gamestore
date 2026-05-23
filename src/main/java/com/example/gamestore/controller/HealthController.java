package com.example.gamestore.controller;

import com.example.gamestore.service.HealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return healthService.isHealthy()
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(503).build();
    }
}
