package com.wictor.repository;

import com.wictor.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    boolean existsByCref(String cref);
}
