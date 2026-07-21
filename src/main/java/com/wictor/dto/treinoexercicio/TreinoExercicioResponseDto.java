package com.wictor.dto.treinoexercicio;

public record TreinoExercicioResponseDto(

        Integer treinoId,
        String treinoNome,
        Integer exercicioId,
        String exercicioNome,
        Integer ordem,
        String carga,
        String series,
        String rep,
        String obs
){}
