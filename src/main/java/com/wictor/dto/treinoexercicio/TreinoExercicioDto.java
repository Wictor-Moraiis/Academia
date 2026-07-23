package com.wictor.dto.treinoexercicio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados necessários para vincular um exercício a um treino")
public record TreinoExercicioDto(

        @Schema(description = "ID do treino", example = "1")
        @NotNull(message = "Treino é obrigatório")
        Integer treinoId,

        @Schema(description = "ID do exercício", example = "5")
        @NotNull(message = "Exercício é obrigatório")
        Integer exercicioId,

        @Schema(description = "Ordem de execução do exercício no treino", example = "1")
        @NotNull(message = "Número de ordem é obrigatório")
        @Positive(message = "Número da ordem deve ser positivo")
        Integer ordem,

        @Schema(description = "Carga utilizada no exercício", example = "40 kg")
        @NotBlank(message = "Carga é obrigatória")
        String carga,

        @Schema(description = "Quantidade de séries", example = "4")
        @NotBlank(message = "Número de séries é obrigatório")
        String series,

        @Schema(description = "Quantidade de repetições", example = "12")
        @NotBlank(message = "Número de repetições é obrigatório")
        String rep,

        @Schema(description = "Observações sobre a execução", example = "Última série até a falha")
        String obs
){}