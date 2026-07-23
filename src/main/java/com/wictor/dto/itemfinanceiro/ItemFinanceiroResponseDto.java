package com.wictor.dto.itemfinanceiro;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados retornados de um item de venda")
public record ItemFinanceiroResponseDto(

        @Schema(description = "Nome do produto", example = "Whey Protein 900g")
        String produtoNome,

        @Schema(description = "Quantidade vendida", example = "2")
        Integer qtd,

        @Schema(description = "Valor unitário do produto", example = "89.90")
        BigDecimal valorUnitario,

        @Schema(description = "Desconto aplicado ao item", example = "10.00")
        BigDecimal desconto,

        @Schema(description = "Valor total do item após o desconto", example = "169.80")
        BigDecimal subtotal
){}