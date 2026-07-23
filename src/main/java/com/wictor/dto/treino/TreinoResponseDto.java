package com.wictor.dto.treino;

import com.wictor.enums.ObjetivoTreino;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados retornados de um treino")
public record TreinoResponseDto(

        @Schema(description = "Identificador do treino", example = "1")
        Integer id,

        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        String nome,

        @Schema(description = "Objetivo principal do treino", example = "HIPERTROFIA")
        ObjetivoTreino ObjTreino,

        @Schema(description = "Data de início do treino", example = "2026-07-22")
        LocalDate inicio,

        @Schema(description = "Data de término do treino", example = "2026-08-22")
        LocalDate fim,

        @Schema(description = "Data de criação do treino", example = "2026-07-22")
        LocalDate criado,

        @Schema(description = "Data da última modificação", example = "2026-07-25")
        LocalDate modificado,

        @Schema(description = "Indica se o treino está ativo", example = "true")
        Boolean ativo,

        @Schema(description = "Nome do aluno", example = "João Silva")
        String alunoNome
){}