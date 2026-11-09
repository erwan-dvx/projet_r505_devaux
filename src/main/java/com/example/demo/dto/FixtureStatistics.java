package com.example.demo.dto;

public record FixtureStatistics(
        int total,
        int won,
        int draw,
        int lost,
        double percentWon,
        double percentDraw,
        double percentLost) {
}
