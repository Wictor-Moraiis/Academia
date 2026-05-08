package com.wictor.Dto;

import com.wictor.enums.ObjetivoTreino;
import java.time.LocalDate;

public record TreinoUpdateDto (

        String nome,
        ObjetivoTreino ObjTreino,
        LocalDate inicio,
        LocalDate fim,
        LocalDate criado,
        LocalDate modificado,
        String obs,
        Boolean ativo,
        Integer alunoId
) {
}

