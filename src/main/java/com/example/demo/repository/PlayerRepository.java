package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Player;
import com.example.demo.model.Statut;


// Le repository permets d'avoir les requêtes SQL de base (CRUD) pour l'entité Player
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByStatut(Statut statut);
    boolean existsByNumLicense(int numLicense);
    Optional<Player> findByNumLicense(int numLicense);
}
