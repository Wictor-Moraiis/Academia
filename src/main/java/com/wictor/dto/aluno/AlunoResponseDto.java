package com.wictor.dto.aluno;

import com.wictor.dto.user.UserResponseDto;
import com.wictor.enums.ObjetivoTreino;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados retornados de um aluno")
public record AlunoResponseDto(

        @Schema(description = "Dados do usuário")
        UserResponseDto user,

        @Schema(description = "Altura do aluno em metros", example = "1.78")
        BigDecimal altura,

        @Schema(description = "Peso do aluno em quilogramas", example = "82.5")
        BigDecimal peso,

        @Schema(description = "Objetivo principal do treinamento", example = "HIPERTROFIA")
        ObjetivoTreino objetivo,

        @Schema(description = "Nome do plano ativo", example = "Plano Premium")
        String planoNome,

        @Schema(description = "Data de vencimento do plano", example = "2026-08-22")
        LocalDate planoVencimento,

        @Schema(description = "Indica se o plano está vencido", example = "false")
        boolean vencido
){}