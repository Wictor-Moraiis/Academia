package com.wictor.dto.plano;

import com.wictor.enums.CicloPlano;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para atualização de um plano")
public record PlanoUpdateDto(

        @Schema(description = "Novo nome do plano", example = "Plano Premium Plus")
        String nome,

        @Schema(description = "Novo valor do plano", example = "119.90")
        @Positive(message = "Valor do plano deve ser positivo")
        BigDecimal valor,

        @Schema(description = "Novo ciclo de cobrança", example = "ANUAL")
        CicloPlano ciclo,

        @Schema(description = "Indica se o plano será recorrente",example = "true")
        Boolean recorrente,

        @Schema(description = "Indica se o plano está ativo", example = "true")
        Boolean ativo
){}