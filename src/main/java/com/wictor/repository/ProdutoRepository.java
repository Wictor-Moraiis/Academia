package com.wictor.repository;

import com.wictor.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    Optional<Produto> findByNomeIgnoreCase(String nome);

    @Query("""
     SELECT p FROM Produto p WHERE p.qtd <= p.qtd_min""")
    List<Produto> buscarProdutosReposicao();
}
