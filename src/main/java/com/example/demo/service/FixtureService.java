package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.FixtureNotFoundException;
import com.example.demo.exception.ParticipationConflictException;
import com.example.demo.model.Fixture;
import com.example.demo.model.Participation;
import com.example.demo.model.Statut;
import com.example.demo.repository.FixtureRepository;
import com.example.demo.repository.ParticipationRepository;

@Service
@Transactional
public class FixtureService {

    private final FixtureRepository fixtureRepository;
    private final ParticipationRepository participationRepository;

    public FixtureService(FixtureRepository fixtureRepository, ParticipationRepository participationRepository) {
        this.fixtureRepository = fixtureRepository;
        this.participationRepository = participationRepository;
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
        fixture.setSheetValidated(false);
        return fixtureRepository.save(fixture);
    }

    public Fixture validateSheet(Long fixtureId) {
        Fixture fixture = findById(fixtureId);
        List<Participation> participations = participationRepository.findByFixtureId(fixtureId);
        for (int jerseyNumber = 1; jerseyNumber <= 15; jerseyNumber++) {
            int requiredNumber = jerseyNumber;
            boolean filled = participations.stream().anyMatch(participation ->
                    participation.getNumberJersey() == requiredNumber
                            && participation.getPlayer() != null
                            && participation.getPlayer().getStatut() == Statut.AVAILABLE);
            if (!filled) {
                throw new ParticipationConflictException(
                        "The 15 starting positions must be assigned to available players");
            }
        }
        fixture.setSheetValidated(true);
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
