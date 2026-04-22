package com.wictor.Dto;

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
        String objetivo,
        Integer planoId,
        LocalDate vencimento,
        Boolean vencido
){}