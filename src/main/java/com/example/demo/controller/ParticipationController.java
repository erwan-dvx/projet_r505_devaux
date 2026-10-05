package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Participation;
import com.example.demo.service.ParticipationService;

@RestController
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping("/{id}")
    public Participation getParticipation(@PathVariable Long id) {
        return participationService.findById(id);
    }

    @GetMapping("/fixtures/{fixtureId}")
    public List<Participation> getParticipationsByFixture(@PathVariable Long fixtureId) {
        return participationService.findByFixtureId(fixtureId);
    }

    @PatchMapping("/{id}/player")
    public Participation assignPlayer(
            @PathVariable Long id,
            @RequestBody PlayerRequest request) {
        if (request == null || request.playerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "playerId is required");
        }
        return participationService.assignPlayer(id, request.playerId());
    }

    @DeleteMapping("/{id}/player")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlayer(@PathVariable Long id) {
        participationService.removePlayer(id);
    }

    @PatchMapping("/{id}/note")
    public Participation updateNote(
            @PathVariable Long id,
            @RequestBody NoteRequest request) {
        if (request == null || request.note() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "note is required");
        }
        return participationService.updateNote(id, request.note());
    }

    public record PlayerRequest(Long playerId) {
    }

    public record NoteRequest(Integer note) {
    }
}