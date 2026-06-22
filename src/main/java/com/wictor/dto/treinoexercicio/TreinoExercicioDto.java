package com.wictor.dto.treinoexercicio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TreinoExercicioDto(

        @NotNull(message = "Treino é obrigatório")
        Integer treinoId,

        @NotNull(message = "Exercício é obrigatório")
        Integer exercicioId,

        @NotNull(message = "Número de ordem é obrigatório")
        @Positive(message = "Número da ordem deve ser positivo")
        Integer ordem,

        @NotBlank(message = "Carga é obrigatória")
        String carga,

        @NotBlank(message = "Número de séries é obrigatório")
        String series,

        @NotBlank(message = "Número de repetições é obrigatório")
        String rep,

        String obs
) {}
