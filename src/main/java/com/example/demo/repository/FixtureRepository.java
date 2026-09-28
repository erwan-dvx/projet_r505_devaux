package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Fixture;

public interface FixtureRepository extends JpaRepository<Fixture, Long>{
    
}
