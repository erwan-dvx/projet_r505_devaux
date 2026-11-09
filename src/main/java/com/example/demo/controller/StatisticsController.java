package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FixtureStatistics;
import com.example.demo.dto.PlayerStatistics;
import com.example.demo.service.StatisticsService;

@RestController
@RequestMapping("/statistiques")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/players/{playerId}")
    public PlayerStatistics getPlayerStatistics(@PathVariable Long playerId) {
        return statisticsService.getPlayerStatistics(playerId);
    }

    @GetMapping("/fixtures")
    public FixtureStatistics getFixtureStatistics() {
        return statisticsService.getFixtureStatistics();
    }
}
