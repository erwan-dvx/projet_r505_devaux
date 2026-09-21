package com.example.demo.model;

public enum Statut {
    AVAILABLE("Disponible"),
    INJURED("Blessé"),
    SUSPENDED("Suspendu"),
    ABSENT("Absent");

    private final String label; 

    Statut(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}