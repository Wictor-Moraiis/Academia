package com.wictor.dto.maquina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados necessários para cadastrar uma máquina")
public record MaquinaDto(

        @Schema(description = "Nome da máquina", example = "Leg Press 45°")
        @NotBlank(message = "Nome da máquina é obrigatório")
        String nome
){}