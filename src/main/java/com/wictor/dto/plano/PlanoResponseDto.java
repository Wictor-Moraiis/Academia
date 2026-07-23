package com.wictor.dto.plano;

import com.wictor.enums.CicloPlano;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados retornados de um plano")
public record PlanoResponseDto(

        @Schema(description = "Identificador do plano", example = "1")
        Integer id,

        @Schema(description = "Nome do plano", example = "Plano Premium")
        String nome,

        @Schema(description = "Valor do plano", example = "99.90")
        BigDecimal valor,

        @Schema(description = "Ciclo de cobrança do plano", example = "MENSAL")
        CicloPlano ciclo,

        @Schema(description = "Indica se o plano é recorrente", example = "true")
        Boolean recorrente
){}