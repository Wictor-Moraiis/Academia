package com.wictor.repository;

import com.wictor.Dto.AlunoResponseDto;
import com.wictor.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlunoRepository  extends JpaRepository<Aluno, Integer> {

    @Query("""
    SELECT new com.wictor.Dto.AlunoResponseDto(
        a.id,
        u.nome,
        a.objetivo,
        a.altura,
        a.peso,
        p.nome
    )
    FROM Aluno a
    JOIN a.user u
    JOIN a.plano p
    WHERE a.id = :id
""")
    AlunoResponseDto buscarAlunoCompleto(Integer id);

    boolean existsByUserId(Integer userId);
}
