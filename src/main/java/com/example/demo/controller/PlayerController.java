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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Player;
import com.example.demo.model.Statut;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.exception.PlayerAlreadyExistsException;


@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerRepository repository;

    public PlayerController(PlayerRepository playerRepository) {
        this.repository = playerRepository;
    }
    
    @GetMapping
    public List<Player> getAllPlayers(@RequestParam(required = false) Statut statut) {
        if (statut == null) {
            return this.repository.findAll();
        }
        return this.repository.findByStatut(statut);
    }

    @GetMapping("/{id}")    
    public Player getPlayerById(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }
    

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        int numLicence = player.getNumLicense();
        if (this.repository.existsByNumLicense(numLicence)) {
            throw new PlayerAlreadyExistsException(numLicence);
        }

        Player saved = this.repository.save(player);
        URI location = URI.create("/players/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player updated) {
        Player player = this.repository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));

        // La licence ne doit pas appartenir à un autre joueur
        this.repository.findByNumLicense(updated.getNumLicense())
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
        player.setStatut(updated.getStatut());

        return this.repository.save(player);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        if (!this.repository.existsById(id)) {
            throw new PlayerNotFoundException(id);
        }
        this.repository.deleteById(id);
    }
}
