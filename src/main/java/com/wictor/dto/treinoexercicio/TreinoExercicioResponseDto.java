package com.wictor.dto.treinoexercicio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados de um exercício vinculado a um treino")
public record TreinoExercicioResponseDto(

        @Schema(description = "ID do treino", example = "1")
        Integer treinoId,

        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        String treinoNome,

        @Schema(description = "ID do exercício", example = "5")
        Integer exercicioId,

        @Schema(description = "Nome do exercício", example = "Supino Reto")
        String exercicioNome,

        @Schema(description = "Ordem de execução no treino", example = "1")
        Integer ordem,

        @Schema(description = "Carga utilizada", example = "40 kg")
        String carga,

        @Schema(description = "Quantidade de séries", example = "4")
        String series,

        @Schema(description = "Quantidade de repetições", example = "12")
        String rep,

        @Schema(description = "Observações", example = "Última série até a falha")
        String obs
){}