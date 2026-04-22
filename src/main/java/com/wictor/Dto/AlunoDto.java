package com.wictor.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlunoDto(

        @NotNull(message = "Identificador de usuário é obrigatório")
        Integer userId,
        @NotBlank(message = "Informações sobre saúde são obrigatórias")
        String saude,
        String obs,
        @Positive(message = "Altura do aluno  deve ser positivo")
        @NotNull(message = "Altura é obrigatória")
        BigDecimal altura,
        @Positive(message = "Peso do aluno  deve ser positivo")
        @NotNull(message = "Peso é obrigatório")
        BigDecimal peso,
        @NotBlank(message = "Objetivo dos treinos é obrigatório")
        String objetivo,
        @NotNull(message = "Id do plano é obrigatório")
        Integer planoId,
        @NotNull(message = "Data de vencimento do plano é obrigatória")
        LocalDate vencimento,
        @NotNull(message = "Validação se o plano está vencido é obrigatória")
        Boolean vencido
) {
}