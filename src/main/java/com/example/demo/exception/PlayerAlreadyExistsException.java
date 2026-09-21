package com.example.demo.exception;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(int numLicense) {
        super("Player already exists with license number: " + numLicense);
    }
}