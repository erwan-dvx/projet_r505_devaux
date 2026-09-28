package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.FixtureNotFoundException;
import com.example.demo.model.Fixture;
import com.example.demo.repository.FixtureRepository;

@RestController 
@RequestMapping("/fixtures")
public class FixtureController {
    private final FixtureRepository repository;

    public FixtureController(FixtureRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Fixture> getAllFixtures() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Fixture getFixtureBydId(@PathVariable Long id) {
        return this.repository
            .findById(id)
            .orElseThrow(() -> new FixtureNotFoundException(id));
    }

    @PostMapping 
    public ResponseEntity<Fixture> createFixture(@RequestBody Fixture fixture) {
        Fixture saved = this.repository.save(fixture);
        URI location = URI.create("/fixtures/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Fixture updateFixture(@PathVariable Long id, @RequestBody Fixture updated) {
        Fixture fixture = this.repository
            .findById(id)
            .orElseThrow(() -> new FixtureNotFoundException(id));

        fixture.setDate(updated.getDate());
        fixture.setNameOpponent(updated.getNameOpponent());
        fixture.setAdress(updated.getAdress());
        fixture.setAtHome(updated.getAtHome());
        fixture.setScoreHome(updated.getScoreHome());
        fixture.setScoreOutside(updated.getScoreOutside());

        return this.repository.save(fixture);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFixture(@PathVariable Long id) {
        if (!this.repository.existsById(id)) {
            throw new FixtureNotFoundException(id);
        }

        this.repository.deleteById(id);
    }
}
