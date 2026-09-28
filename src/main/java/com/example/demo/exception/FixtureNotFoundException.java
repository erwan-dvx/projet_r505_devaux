package com.example.demo.exception;

public class FixtureNotFoundException extends RuntimeException {
    public FixtureNotFoundException(Long id) {
        super("Fixture not found with id : " + id);
    }
}