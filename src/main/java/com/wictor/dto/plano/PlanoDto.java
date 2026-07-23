package com.wictor.dto.plano;

import com.wictor.enums.CicloPlano;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados necessários para cadastrar um plano")
public record PlanoDto(

        @Schema(description = "Nome do plano", example = "Plano Premium")
        @NotBlank(message = "Nome do plano é obrigatório")
        String nome,

        @Schema(description = "Valor do plano", example = "99.90")
        @NotNull(message = "Valor do plano é obrigatório")
        @Positive(message = "Valor do plano deve ser positivo")
        BigDecimal valor,

        @Schema(description = "Ciclo de cobrança do plano", example = "MENSAL")
        @NotNull(message = "Ciclo do plano é obrigatório")
        CicloPlano ciclo,

        @Schema(description = "Indica se o plano é recorrente", example = "true")
        @NotNull(message = "Recorrencia do plano é obrigatória")
        Boolean recorrente
){}