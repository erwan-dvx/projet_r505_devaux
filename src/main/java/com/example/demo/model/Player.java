package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String firstName;
    private int numLicense;
    private LocalDate dateOfBirth;
    private int size;
    private float weight;
    @Enumerated(EnumType.STRING)
    private Statut statut;

    // JPA/Hibernate a besoin d'un constructeur vide pour instancier l'entité
    protected Player() {}

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public int getNumLicense() {
        return this.numLicense;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public int getSize() {
        return this.size;
    }

    public float getWeight() {
        return this.weight;
    }

    public Statut getStatut() {
        return this.statut;
    }

    public String getStatutLabel() {
        return this.statut != null ? this.statut.getLabel() : null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setNumLicense(int numLicense) {
        this.numLicense = numLicense;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}


