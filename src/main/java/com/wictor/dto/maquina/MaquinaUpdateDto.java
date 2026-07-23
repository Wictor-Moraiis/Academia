package com.wictor.dto.maquina;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de uma máquina")
public record MaquinaUpdateDto(

        @Schema(description = "Novo nome da máquina", example = "Leg Press Horizontal")
        String nome,

        @Schema(description = "Indica se a máquina está ativa", example = "true")
        Boolean ativa
){}