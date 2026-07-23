package com.wictor.dto.exercicio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados necessários para cadastrar um exercício")
public record ExercicioDto(

        @Schema(description = "Nome do exercício", example = "Supino Reto")
        @NotBlank(message = "Nome do Exercicio é obrigatório")
        String nome,

        @Schema(description = "Observações sobre a execução do exercício",
                example = "Executar lentamente durante a fase excêntrica")
        String obs,

        @Schema(description = "ID da máquina utilizada no exercício", example = "3")
        Integer maquinaId
){}