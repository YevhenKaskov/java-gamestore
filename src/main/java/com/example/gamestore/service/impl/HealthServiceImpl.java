package com.example.gamestore.service.impl;

import com.example.gamestore.service.HealthService;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public boolean isHealthy() {
        return true;
    }
}
