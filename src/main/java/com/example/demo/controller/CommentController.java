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

import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;

@RestController 
@RequestMapping("/players/{playerId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long playerId) {
        return commentService.findByPlayerId(playerId);
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long playerId, @PathVariable Long id) {
        return commentService.findById(playerId, id);
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long playerId, @RequestBody Comment comment) {
        Comment saved = commentService.create(playerId, comment);
        URI location = URI.create("/players/" + playerId + "/comments/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long playerId, @PathVariable Long id, @RequestBody Comment updated) {
        return commentService.update(playerId, id, updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long playerId, @PathVariable Long id) {
        commentService.delete(playerId, id);
    }
}
