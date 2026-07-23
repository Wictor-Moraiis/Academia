package com.wictor.dto.itemfinanceiro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Dados de um item utilizado em uma venda")
public record ItemFinanceiroDto(

        @Schema(description = "ID do produto vendido", example = "5")
        @NotNull(message = "Id do produto é obrigatório")
        Integer produtoId,

        @Schema(description = "Quantidade do produto", example = "2")
        @NotNull(message = "Quantidade da venda é obrigatória")
        Integer qtd,

        @Schema(description = "Valor do desconto aplicado ao item", example = "10.00")
        @NotNull(message = "Desconto não pode ser nulo")
        @PositiveOrZero(message = "Desconto não pode ser negativo")
        BigDecimal desconto
){}