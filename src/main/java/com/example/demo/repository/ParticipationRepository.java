package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByFixtureId(Long fixtureId);
    List<Participation> findByPlayerId(Long playerId);
}
