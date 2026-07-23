package com.wictor.dto.treinoexercicio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para atualização de um exercício vinculado a um treino")
public record TreinoExercicioUpdateDto(

        @Schema(description = "Nova ordem de execução", example = "2")
        @Positive(message = "Número da ordem deve ser positivo")
        Integer ordem,

        @Schema(description = "Nova carga", example = "45 kg")
        String carga,

        @Schema(description = "Nova quantidade de séries", example = "5")
        String series,

        @Schema(description = "Nova quantidade de repetições", example = "10")
        String rep,

        @Schema(description = "Novas observações", example = "Reduzir a carga na última série")
        String obs
){}