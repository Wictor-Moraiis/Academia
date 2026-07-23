package com.wictor.dto.aluno;

import com.wictor.dto.user.UserDto;
import com.wictor.enums.ObjetivoTreino;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados necessários para cadastrar um aluno")
public record AlunoDto(

        @Schema(description = "Dados do usuário vinculado ao aluno")
        @NotNull(message = "Dados do usuário são obrigatórios")
        @Valid
        UserDto user,

        @Schema(description = "Informações de saúde relevantes para os treinos",
                example = "Sem restrições médicas")
        @NotBlank(message = "Informações sobre saúde são obrigatórias")
        String saude,

        @Schema(description = "Observações adicionais sobre o aluno",
                example = "Possui disponibilidade apenas no período noturno")
        String obs,

        @Schema(description = "Altura do aluno em metros", example = "1.78")
        @Positive(message = "Altura do aluno deve ser positivo")
        @NotNull(message = "Altura é obrigatória")
        BigDecimal altura,

        @Schema(description = "Peso do aluno em quilogramas", example = "82.5")
        @Positive(message = "Peso do aluno deve ser positivo")
        @NotNull(message = "Peso é obrigatório")
        BigDecimal peso,

        @Schema(description = "Objetivo principal do treinamento", example = "HIPERTROFIA")
        @NotNull(message = "Objetivo dos treinos é obrigatório")
        ObjetivoTreino objetivo
){}