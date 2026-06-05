package com.wictor.dto.treino;

public record TreinoExercicioResponseDto (

        Integer treinoId,
        String treinoNome,

        Integer exercicioId,
        String exercicioNome,

        Integer ordem,

        String carga,
        String series,
        String rep,
        String obs
)
{}
