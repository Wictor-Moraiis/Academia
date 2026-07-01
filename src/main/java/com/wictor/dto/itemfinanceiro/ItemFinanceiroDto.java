package com.wictor.dto.itemfinanceiro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItemFinanceiroDto (

        @NotNull(message = "Id do produto é obrigatório")
        Integer produtoId,

        @NotNull(message = "Quantidade da venda é obrigatória")
        Integer qtd,

        @NotNull(message = "Desconto não pode ser nulo")
        @PositiveOrZero(message = "Desconto não pode ser negativo")
        BigDecimal desconto
){ }
