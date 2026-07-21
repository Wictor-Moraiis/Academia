package com.wictor.dto.maquina;

import jakarta.validation.constraints.NotBlank;

public record MaquinaDto(

        @NotBlank(message = "Nome da máquina  é obrigatório")
        String nome
){}