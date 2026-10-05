package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.FixtureNotFoundException;
import com.example.demo.model.Fixture;
import com.example.demo.repository.FixtureRepository;

@Service
@Transactional
public class FixtureService {

    private final FixtureRepository fixtureRepository;

    public FixtureService(FixtureRepository fixtureRepository) {
        this.fixtureRepository = fixtureRepository;
    }

    @Transactional(readOnly = true)
    public List<Fixture> findAll() {
        return fixtureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Fixture findById(Long id) {
        return fixtureRepository.findById(id)
                .orElseThrow(() -> new FixtureNotFoundException(id));
    }

    public Fixture create(Fixture fixture) {
        return fixtureRepository.save(fixture);
    }

    public Fixture update(Long id, Fixture updated) {
        Fixture fixture = findById(id);

        fixture.setDate(updated.getDate());
        fixture.setNameOpponent(updated.getNameOpponent());
        fixture.setAdress(updated.getAdress());
        fixture.setAtHome(updated.getAtHome());
        fixture.setScoreHome(updated.getScoreHome());
        fixture.setScoreOutside(updated.getScoreOutside());

        return fixtureRepository.save(fixture);
    }

    public void delete(Long id) {
        fixtureRepository.delete(findById(id));
    }
}