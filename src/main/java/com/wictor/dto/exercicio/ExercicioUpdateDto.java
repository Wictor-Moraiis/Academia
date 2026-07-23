package com.wictor.dto.exercicio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de um exercício")
public record ExercicioUpdateDto(

        @Schema(description = "Novo nome do exercício", example = "Supino Inclinado")
        String nome,

        @Schema(description = "Novas observações sobre o exercício",
                example = "Manter os cotovelos alinhados durante a execução")
        String obs,

        @Schema(description = "ID da nova máquina associada ao exercício", example = "5")
        Integer maquinaId
){}