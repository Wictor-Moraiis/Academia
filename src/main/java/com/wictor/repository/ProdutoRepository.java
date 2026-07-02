package com.wictor.repository;

import com.wictor.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    Optional<Produto> findByNomeIgnoreCase(String nome);
}
