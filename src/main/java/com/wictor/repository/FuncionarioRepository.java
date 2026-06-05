package com.wictor.repository;

import com.wictor.dto.funcionario.FuncionarioResponseDto;
import com.wictor.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    @Query("""
                SELECT new com.wictor.Dto.FuncionarioResponseDto(
                     f.id,
                     u.nome,
                     f.cref,
                     f.tipoContrato,
                     f.turnoIni,
                     f.turnoFim,
                     c.nome,
                     c.salario
                )
                FROM Funcionario f
                JOIN f.user u
                JOIN f.categoria c
                WHERE f.id = :id
            """)
    FuncionarioResponseDto buscarFuncionarioCompleto(Integer id);
    boolean existsByCref(String cref);
    boolean existsByUserId(Integer id);
}
