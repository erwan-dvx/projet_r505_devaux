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
import com.example.demo.service.PlayerService;


@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @GetMapping
    public List<Player> getAllPlayers(@RequestParam(required = false) Statut statut) {
        return playerService.findAll(statut);
    }

    @GetMapping("/{id}")    
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.findById(id);
    }
    

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player saved = playerService.create(player);
        URI location = URI.create("/players/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player updated) {
        return playerService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerService.delete(id);
    }
}
