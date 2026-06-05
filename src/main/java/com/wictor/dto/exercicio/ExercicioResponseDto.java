package com.wictor.dto.exercicio;

public record ExercicioResponseDto(
        Integer id,
        String nome,
        String obs,
        String maquinaNome
) {}
