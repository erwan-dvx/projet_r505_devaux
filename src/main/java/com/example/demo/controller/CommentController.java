package com.example.demo.controller;

import java.net.URI;
import java.time.LocalDateTime;
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

import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.model.Player;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PlayerRepository;

@RestController 
@RequestMapping("/players/{playerId}/comments")
public class CommentController {
    private final CommentRepository repository;
    private final PlayerRepository playerRepository;

    public CommentController(CommentRepository repository, PlayerRepository playerRepository) {
        this.repository = repository;
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long playerId) {
        if (!this.playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        return this.repository.findByPlayerId(playerId);
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long playerId, @PathVariable Long id) {
        if (!this.playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }
        
        return this.repository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long playerId, @RequestBody Comment comment) {
        Player player = this.playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId));
        
        comment.setPlayer(player);
        comment.setDate(LocalDateTime.now());

        Comment saved = this.repository.save(comment);
        URI location = URI.create("/players/" + playerId + "/comments/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long playerId, @PathVariable Long id, @RequestBody Comment updated) {
        if (!this.playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        Comment comment = this.repository.findById(id)
            .orElseThrow(() -> new CommentNotFoundException(id));

        comment.setContent(updated.getContent());
        // On ne change pas la date car on prends la date de création 
        
        return this.repository.save(comment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long playerId, @PathVariable Long id) {
        if (!this.playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }
        
        if (!this.repository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }

        this.repository.deleteById(id);
    }
}
