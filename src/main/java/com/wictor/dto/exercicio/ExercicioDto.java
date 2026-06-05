package com.wictor.dto.exercicio;

import jakarta.validation.constraints.NotBlank;

public record ExercicioDto(

        @NotBlank(message = "Nome do Exercicio é obrigatório")
        String nome,

        String obs,

        Integer maquinaId
) {
}