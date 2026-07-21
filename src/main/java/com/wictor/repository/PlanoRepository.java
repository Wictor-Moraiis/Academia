package com.wictor.repository;

import com.wictor.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {

    Optional<Plano> findByAbacateProductId(String id);
}