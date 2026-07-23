package com.wictor.dto.treino;

import com.wictor.enums.ObjetivoTreino;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados para atualização de um treino")
public record TreinoUpdateDto(

        @Schema(description = "Novo nome do treino", example = "Treino B - Costas e Bíceps")
        String nome,

        @Schema(description = "Novo objetivo do treino", example = "RESISTENCIA")
        ObjetivoTreino ObjTreino,

        @Schema(description = "Nova data de início", example = "2026-07-25")
        LocalDate inicio,

        @Schema(description = "Nova data de término", example = "2026-08-25")
        LocalDate fim,

        @Schema(description = "Data de criação do treino", example = "2026-07-22")
        LocalDate criado,

        @Schema(description = "Data da última modificação", example = "2026-07-26")
        LocalDate modificado,

        @Schema(description = "Novas observações",
                example = "Aumentar carga progressivamente")
        String obs,

        @Schema(description = "Indica se o treino está ativo", example = "true")
        Boolean ativo,

        @Schema(description = "ID do aluno vinculado ao treino", example = "15")
        Integer alunoId
){}