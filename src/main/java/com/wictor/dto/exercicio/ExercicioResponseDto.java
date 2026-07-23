package com.wictor.dto.exercicio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados de um exercício")
public record ExercicioResponseDto(

        @Schema(description = "Identificador do exercício", example = "1")
        Integer id,

        @Schema(description = "Nome do exercício", example = "Supino Reto")
        String nome,

        @Schema(description = "Observações sobre o exercício",
                example = "Executar lentamente durante a fase excêntrica")
        String obs,

        @Schema(description = "Nome da máquina utilizada", example = "Banco de Supino")
        String maquinaNome
){}