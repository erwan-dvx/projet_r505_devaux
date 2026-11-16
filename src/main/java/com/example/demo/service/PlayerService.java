package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.PlayerAlreadyExistsException;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Player;
import com.example.demo.model.Statut;
import com.example.demo.repository.ParticipationRepository;
import com.example.demo.repository.PlayerRepository;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ParticipationRepository participationRepository;

    public PlayerService(PlayerRepository playerRepository, ParticipationRepository participationRepository) {
        this.playerRepository = playerRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional(readOnly = true)
    public List<Player> findAll(Statut statut) {
        if (statut == null) {
            return playerRepository.findAll();
        }
        return playerRepository.findByStatut(statut);
    }

    @Transactional(readOnly = true)
    public Player findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Player create(Player player) {
        int numLicense = player.getNumLicense();
        if (playerRepository.existsByNumLicense(numLicense)) {
            throw new PlayerAlreadyExistsException(numLicense);
        }
        return playerRepository.save(player);
    }

    public Player update(Long id, Player updated) {
        Player player = findById(id);

        playerRepository.findByNumLicense(updated.getNumLicense())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new PlayerAlreadyExistsException(updated.getNumLicense());
                });

        player.setName(updated.getName());
        player.setFirstName(updated.getFirstName());
        player.setNumLicense(updated.getNumLicense());
        player.setDateOfBirth(updated.getDateOfBirth());
        player.setSize(updated.getSize());
        player.setWeight(updated.getWeight());
        if (player.getStatut() == Statut.AVAILABLE && updated.getStatut() != Statut.AVAILABLE) {
            invalidateSheets(id);
        }
        player.setStatut(updated.getStatut());

        return playerRepository.save(player);
    }

    public void delete(Long id) {
        Player player = findById(id);
        invalidateSheets(id);
        playerRepository.delete(player);
    }

    private void invalidateSheets(Long playerId) {
        participationRepository.findByPlayerId(playerId).forEach(participation ->
                participation.getFixture().setSheetValidated(false));
    }
}
