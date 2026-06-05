package com.wictor.dto.treinoexercicio;

import jakarta.validation.constraints.Positive;

public record TreinoExercicioUpdateDto(

        @Positive(message = "Número da ordem deve ser positivo")
        Integer ordem,

        String carga,

        String series,

        String rep,

        String obs
) {}
