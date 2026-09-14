package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class BonjourControleur {
    
    @GetMapping("/bonjour")
    public String bonjour() {
        return "Bonjour le monde !";
    }
    
}