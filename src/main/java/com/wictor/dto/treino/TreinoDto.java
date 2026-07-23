package com.wictor.dto.treino;

import com.wictor.enums.ObjetivoTreino;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Dados necessários para cadastrar um treino")
public record TreinoDto(

        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        @NotBlank(message = "Nome do treino é obrigatório")
        String nome,

        @Schema(description = "Objetivo principal do treino", example = "HIPERTROFIA")
        @NotNull(message = "Objetivo do treino é obrigatório")
        ObjetivoTreino ObjTreino,

        @Schema(description = "Data de início do treino", example = "2026-07-22")
        @NotNull(message = "Data de inicio do treino é obrigatório")
        LocalDate inicio,

        @Schema(description = "Data de término do treino", example = "2026-08-22")
        @NotNull(message = "Data de fim do treino é obrigatório")
        LocalDate fim,

        @Schema(description = "Data de criação do treino", example = "2026-07-22")
        LocalDate criado,

        @Schema(description = "Data da última modificação", example = "2026-07-25")
        LocalDate modificado,

        @Schema(description = "Observações sobre o treino",
                example = "Descansar 60 segundos entre as séries")
        String obs,

        @Schema(description = "Indica se o treino está ativo", example = "true")
        Boolean ativo,

        @Schema(description = "ID do aluno que receberá o treino", example = "15")
        Integer alunoId
){}