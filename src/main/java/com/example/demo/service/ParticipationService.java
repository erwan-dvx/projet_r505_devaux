package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.FixtureNotFoundException;
import com.example.demo.exception.ParticipationConflictException;
import com.example.demo.exception.ParticipationNotFoundException;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Fixture;
import com.example.demo.model.Participation;
import com.example.demo.model.Player;
import com.example.demo.model.Position;
import com.example.demo.model.Statut;
import com.example.demo.repository.FixtureRepository;
import com.example.demo.repository.ParticipationRepository;
import com.example.demo.repository.PlayerRepository;

@Service
@Transactional
public class ParticipationService {

	private final ParticipationRepository participationRepository;
	private final FixtureRepository fixtureRepository;
	private final PlayerRepository playerRepository;

	public ParticipationService(
			ParticipationRepository participationRepository,
			FixtureRepository fixtureRepository,
			PlayerRepository playerRepository) 
    {
		this.participationRepository = participationRepository;
		this.fixtureRepository = fixtureRepository;
		this.playerRepository = playerRepository;
	}

	@Transactional(readOnly = true)
	public Participation findById(Long participationId) {
		return participationRepository.findById(participationId)
				.orElseThrow(() -> new ParticipationNotFoundException(participationId));
	}

	@Transactional(readOnly = true)
	public List<Participation> findByFixtureId(Long fixtureId) {
		getFixture(fixtureId);
		return participationRepository.findByFixtureId(fixtureId);
	}

	public Participation create(Long fixtureId, Position namePosition, Long playerId) {
		Fixture fixture = getFixture(fixtureId);
		if (namePosition == null) {
			throw new ParticipationConflictException("The position is required");
		}

		ensureBeforeFixture(fixture, "A participation cannot be created after the fixture");

		boolean positionAlreadyUsed = participationRepository.findByFixtureId(fixtureId).stream()
				.anyMatch(existing -> namePosition == existing.getNamePosition());
		if (positionAlreadyUsed) {
			throw new ParticipationConflictException("The position is already used for this fixture");
		}

		Player player = playerId == null ? null : getPlayer(playerId);
		if (player != null) {
			if (player.getStatut() != Statut.AVAILABLE) {
				throw new ParticipationConflictException("The player is not available");
			}

			boolean playerAlreadyAssigned = participationRepository.findByFixtureId(fixtureId).stream()
					.anyMatch(existing -> player.equals(existing.getPlayer()));
			if (playerAlreadyAssigned) {
				throw new ParticipationConflictException("The player is already assigned to this fixture");
			}
		}

		Participation participation = new Participation(fixture, player, namePosition);
		return participationRepository.save(participation);
	}

	public Participation assignPlayer(Long participationId, Long playerId) {
		Participation participation = findById(participationId);
		Fixture fixture = getFixture(participation.getFixture().getId());
		Player player = getPlayer(playerId);

		ensureBeforeFixture(fixture, "A player cannot be assigned after the fixture");

		if (player.getStatut() != Statut.AVAILABLE) {
			throw new ParticipationConflictException("The player is not available");
		}

		boolean alreadyAssigned = participationRepository.findByFixtureId(fixture.getId()).stream()
				.anyMatch(existing -> player.equals(existing.getPlayer())
						&& !existing.getId().equals(participation.getId()));
		if (alreadyAssigned) {
			throw new ParticipationConflictException("The player is already assigned to this fixture");
		}

		participation.setPlayer(player);
		return participationRepository.save(participation);
	}

	public Participation removePlayer(Long participationId) {
		Participation participation = findById(participationId);
		ensureBeforeFixture(participation.getFixture(), "A player cannot be removed after the fixture");

		participation.setPlayer(null);
		return participationRepository.save(participation);
	}

	public Participation updateNote(Long participationId, Integer note) {
		Participation participation = findById(participationId);

		if (participation.getFixture().getDate() == null
				|| !LocalDateTime.now().isAfter(participation.getFixture().getDate())) {
			throw new ParticipationConflictException("A participation note can only be changed after the fixture");
		}
		if (participation.getPlayer() == null) {
			throw new ParticipationConflictException("A participation without a player cannot have a note");
		}
		if (note == null) {
			throw new ParticipationConflictException("The note cannot be null");
		}

		participation.setNote(note);
		return participationRepository.save(participation);
	}

	private Fixture getFixture(Long fixtureId) {
		return fixtureRepository.findById(fixtureId)
				.orElseThrow(() -> new FixtureNotFoundException(fixtureId));
	}

	private Player getPlayer(Long playerId) {
		return playerRepository.findById(playerId)
				.orElseThrow(() -> new PlayerNotFoundException(playerId));
	}

	private void ensureBeforeFixture(Fixture fixture, String message) {
		if (fixture.getDate() != null && !LocalDateTime.now().isBefore(fixture.getDate())) {
			throw new ParticipationConflictException(message);
		}
	}
}
