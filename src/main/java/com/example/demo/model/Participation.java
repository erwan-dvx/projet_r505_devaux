package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "participation")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Integer note;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position namePosition;

    @Column(nullable = false)
    private int numberJersey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Fixture fixture;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Player player;

    protected Participation() {}

    public Long getId() {
        return this.id;
    }

    public int getNumberJersey() {
        return this.numberJersey;
    }

    public Integer getNote() {
        return this.note;
    }

    public Position getNamePosition() {
        return this.namePosition;
    }

    public Fixture getFixture() {
        return this.fixture;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public void setNamePosition(Position namePosition) {
        this.namePosition = namePosition;
    }

    public void setNumberJersey(int numberJersey) {
        this.numberJersey = numberJersey;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
