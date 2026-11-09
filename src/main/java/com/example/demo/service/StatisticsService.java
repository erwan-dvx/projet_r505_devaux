package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.FixtureStatistics;
import com.example.demo.dto.PlayerStatistics;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Fixture;
import com.example.demo.model.Participation;
import com.example.demo.model.Player;
import com.example.demo.repository.FixtureRepository;
import com.example.demo.repository.ParticipationRepository;
import com.example.demo.repository.PlayerRepository;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final PlayerRepository playerRepository;
    private final ParticipationRepository participationRepository;
    private final FixtureRepository fixtureRepository;

    public StatisticsService(
            PlayerRepository playerRepository,
            ParticipationRepository participationRepository,
            FixtureRepository fixtureRepository) {
        this.playerRepository = playerRepository;
        this.participationRepository = participationRepository;
        this.fixtureRepository = fixtureRepository;
    }

    public PlayerStatistics getPlayerStatistics(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
        List<Fixture> completedFixtures = getCompletedFixtures();
        Set<Long> completedFixtureIds = completedFixtures.stream()
                .map(Fixture::getId)
                .collect(Collectors.toSet());
        List<Participation> participations = participationRepository.findByPlayerId(playerId).stream()
                .filter(participation -> completedFixtureIds.contains(participation.getFixture().getId()))
                .toList();

        int starters = (int) participations.stream()
                .filter(participation -> participation.getNumberJersey() >= 1
                        && participation.getNumberJersey() <= 15)
                .count();
        int substitutes = (int) participations.stream()
                .filter(participation -> participation.getNumberJersey() >= 16
                        && participation.getNumberJersey() <= 23)
                .count();
        Double averageRatings = participations.stream()
                .map(Participation::getNote)
                .filter(note -> note != null)
                .mapToInt(Integer::intValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .map(value -> Math.round(value * 100.0) / 100.0)
                .orElse(null);

        String preferredPosition = participations.stream()
                .filter(participation -> participation.getNumberJersey() >= 1
                        && participation.getNumberJersey() <= 15)
                .collect(Collectors.groupingBy(
                        participation -> participation.getNamePosition().getLabel(),
                        Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Non défini");

        long wins = participations.stream()
                .filter(participation -> hasWon(participation.getFixture()))
                .count();
        double winPercentage = participations.isEmpty()
                ? 0
                : Math.round((wins * 100.0 / participations.size()) * 100.0) / 100.0;

        return new PlayerStatistics(
                player.getStatut() == null ? null : player.getStatut().getLabel(),
                preferredPosition,
                starters,
                substitutes,
                averageRatings,
                winPercentage,
                getConsecutiveSelections(playerId, completedFixtures),
                participations.size());
    }

    public FixtureStatistics getFixtureStatistics() {
        List<Fixture> completedFixtures = getCompletedFixtures();
        int won = (int) completedFixtures.stream().filter(this::hasWon).count();
        int lost = (int) completedFixtures.stream().filter(this::hasLost).count();
        int draw = completedFixtures.size() - won - lost;
        int total = completedFixtures.size();

        return new FixtureStatistics(
                total,
                won,
                draw,
                lost,
                percentage(won, total),
                percentage(draw, total),
                percentage(lost, total));
    }

    private List<Fixture> getCompletedFixtures() {
        LocalDateTime now = LocalDateTime.now();
        return fixtureRepository.findAll().stream()
                .filter(fixture -> fixture.getDate() != null
                        && !fixture.getDate().isAfter(now)
                        && fixture.getScoreHome() != null
                        && fixture.getScoreOutside() != null)
                .sorted(Comparator.comparing(Fixture::getDate).reversed())
                .toList();
    }

    private int getConsecutiveSelections(Long playerId, List<Fixture> completedFixtures) {
        Set<Long> selectedFixtureIds = participationRepository.findByPlayerId(playerId).stream()
                .map(participation -> participation.getFixture().getId())
                .collect(Collectors.toSet());
        int consecutive = 0;
        for (Fixture fixture : completedFixtures) {
            if (!selectedFixtureIds.contains(fixture.getId())) {
                break;
            }
            consecutive++;
        }
        return consecutive;
    }

    private boolean hasWon(Fixture fixture) {
        return fixture.getAtHome()
                ? fixture.getScoreHome() > fixture.getScoreOutside()
                : fixture.getScoreOutside() > fixture.getScoreHome();
    }

    private boolean hasLost(Fixture fixture) {
        return fixture.getAtHome()
                ? fixture.getScoreHome() < fixture.getScoreOutside()
                : fixture.getScoreOutside() < fixture.getScoreHome();
    }

    private double percentage(int value, int total) {
        return total == 0 ? 0 : Math.round(value * 1000.0 / total) / 10.0;
    }
}
