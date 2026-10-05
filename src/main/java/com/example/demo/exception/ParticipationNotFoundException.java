package com.example.demo.exception;

public class ParticipationNotFoundException extends RuntimeException {
    public ParticipationNotFoundException(Long id) {
        super("Participation not found with id : " + id);
    }
}