package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sexo informado para o usuário.")
public enum Sexo {

    @Schema(description = "Masculino.")
    M,

    @Schema(description = "Feminino.")
    F
}