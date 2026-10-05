package com.example.demo.exception;

public class ParticipationConflictException extends RuntimeException {
    public ParticipationConflictException(String message) {
        super(message);
    }
}