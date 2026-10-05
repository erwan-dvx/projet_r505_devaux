package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.model.Player;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PlayerRepository;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PlayerRepository playerRepository;

    public CommentService(CommentRepository commentRepository, PlayerRepository playerRepository) {
        this.commentRepository = commentRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPlayerId(Long playerId) {
        getPlayer(playerId);
        return commentRepository.findByPlayerId(playerId);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long playerId, Long commentId) {
        getPlayer(playerId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getPlayer().getId().equals(playerId)) {
            throw new CommentNotFoundException(commentId);
        }
        return comment;
    }

    public Comment create(Long playerId, Comment comment) {
        comment.setPlayer(getPlayer(playerId));
        comment.setDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment update(Long playerId, Long commentId, Comment updated) {
        Comment comment = findById(playerId, commentId);
        comment.setContent(updated.getContent());
        return commentRepository.save(comment);
    }

    public void delete(Long playerId, Long commentId) {
        commentRepository.delete(findById(playerId, commentId));
    }

    private Player getPlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }
}