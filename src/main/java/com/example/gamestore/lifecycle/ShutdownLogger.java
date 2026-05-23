package com.example.gamestore.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ShutdownLogger {

    private static final Logger log = LoggerFactory.getLogger(ShutdownLogger.class);

    @PostConstruct
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("SIGTERM received. Starting graceful shutdown...");
        }));
    }
}