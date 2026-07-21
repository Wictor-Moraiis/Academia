package com.wictor.dto.aluno;

import com.wictor.enums.ObjetivoTreino;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlunoUpdateDto(

        String saude,

        String obs,

        @Positive(message = "Altura do aluno  deve ser positivo")
        BigDecimal altura,

        @Positive(message = "Peso do aluno  deve ser positivo")
        BigDecimal peso,

        ObjetivoTreino objetivo,

        Integer planoId,

        LocalDate vencimento,

        Boolean vencido
){}