package com.wictor.dto.treino;

import com.wictor.enums.ObjetivoTreino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TreinoDto(

        @NotBlank(message = "Nome do treino é obrigatório")
        String nome,
        @NotNull(message = "Objetivo do treino é obrigatório")
        ObjetivoTreino ObjTreino,
        @NotNull(message = "Data de inicio do treino é obrigatório")
        LocalDate inicio,
        @NotNull(message = "Data de fim do treino é obrigatório")
        LocalDate fim,
        LocalDate criado,
        LocalDate modificado,
        String obs,
        Boolean ativo,
        Integer alunoId
) {
}

