package com.wictor.repository;

import com.wictor.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository  extends JpaRepository<Aluno, Integer> {

    boolean existsByUserId(Integer userId);
}
