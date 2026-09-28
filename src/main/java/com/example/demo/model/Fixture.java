package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "fixture")
public class Fixture {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date; 
    private String nameOpponent;
    private String adress;
    private boolean atHome; 
    private int scoreHome; 
    private int scoreOutside;

    protected Fixture() {}

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public String getNameOpponent() {
        return this.nameOpponent;
    }

    public String getAdress() {
        return this.adress;
    }

    public boolean getAtHome() {
        return this.atHome;
    }

    public int getScoreHome() {
        return this.scoreHome;
    }

    public int getScoreOutside() {
        return this.scoreOutside;
    }

    public void setDate(LocalDateTime newDate) {
        this.date = newDate;
    }

    public void setNameOpponent(String nameOpponent) {
        this.nameOpponent = nameOpponent;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setAtHome(boolean atHome) {
        this.atHome = atHome;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public void setScoreOutside(int scoreOutside) {
        this.scoreOutside = scoreOutside;
    }
}
