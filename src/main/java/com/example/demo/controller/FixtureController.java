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

import com.example.demo.model.Fixture;
import com.example.demo.service.FixtureService;

@RestController 
@RequestMapping("/fixtures")
public class FixtureController {
    private final FixtureService fixtureService;

    public FixtureController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @GetMapping
    public List<Fixture> getAllFixtures() {
        return fixtureService.findAll();
    }

    @GetMapping("/{id}")
    public Fixture getFixtureBydId(@PathVariable Long id) {
        return fixtureService.findById(id);
    }

    @PostMapping 
    public ResponseEntity<Fixture> createFixture(@RequestBody Fixture fixture) {
        Fixture saved = fixtureService.create(fixture);
        URI location = URI.create("/fixtures/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Fixture updateFixture(@PathVariable Long id, @RequestBody Fixture updated) {
        return fixtureService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFixture(@PathVariable Long id) {
        fixtureService.delete(id);
    }
}
