package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objetivo principal do treinamento do aluno.")
public enum ObjetivoTreino {

    @Schema(description = "Treinamento focado no aumento de força muscular.")
    FORCA,

    @Schema(description = "Treinamento voltado para ganho de massa muscular.")
    HIPERTROFIA,

    @Schema(description = "Treinamento focado no aumento da resistência física.")
    RESISTENCIA,

    @Schema(description = "Treinamento voltado para melhora da capacidade funcional e condicionamento físico.")
    FUNCIONAL,

    @Schema(description = "Treinamento com foco na redução de gordura corporal.")
    EMAGRECIMENTO,

    @Schema(description = "Treinamento voltado para melhoria da mobilidade e flexibilidade.")
    MOBILIDADE,

    @Schema(description = "Treinamento destinado à recuperação física após lesões ou limitações.")
    REABILITACAO
}