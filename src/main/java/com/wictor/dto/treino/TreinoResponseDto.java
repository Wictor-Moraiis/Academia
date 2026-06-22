package com.wictor.dto.treino;

import com.wictor.enums.ObjetivoTreino;

import java.time.LocalDate;

public record TreinoResponseDto(
        Integer id,
        String nome,
        ObjetivoTreino ObjTreino,
        LocalDate inicio,
        LocalDate fim,
        LocalDate criado,
        LocalDate modificado,
        Boolean ativo,
        String alunoNome
) {
}
