package com.wictor.dto.aluno;

import com.wictor.dto.user.UserDto;
import com.wictor.enums.ObjetivoTreino;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AlunoDto(

        @NotNull(message = "Dados do usuário são obrigatórios")
        @Valid
        UserDto user,

        @NotBlank(message = "Informações sobre saúde são obrigatórias")
        String saude,

        String obs,

        @Positive(message = "Altura do aluno  deve ser positivo")
        @NotNull(message = "Altura é obrigatória")
        BigDecimal altura,

        @Positive(message = "Peso do aluno  deve ser positivo")
        @NotNull(message = "Peso é obrigatório")
        BigDecimal peso,

        @NotNull(message = "Objetivo dos treinos é obrigatório")
        ObjetivoTreino objetivo
){}