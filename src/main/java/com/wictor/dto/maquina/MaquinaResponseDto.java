package com.wictor.dto.maquina;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados de uma máquina")
public record MaquinaResponseDto(

        @Schema(description = "Identificador da máquina", example = "1")
        Integer id,

        @Schema(description = "Nome da máquina", example = "Leg Press 45°")
        String nome,

        @Schema(description = "Indica se a máquina está ativa", example = "true")
        boolean ativa
){}