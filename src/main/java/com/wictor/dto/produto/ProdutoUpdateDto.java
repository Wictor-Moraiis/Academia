package com.wictor.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Dados para atualização de um produto")
public record ProdutoUpdateDto(

        @Schema(description = "Novo nome do produto", example = "Creatina Monohidratada 300g")
        String nome,

        @Schema(description = "Nova descrição do produto",
                example = "Creatina monohidratada 100% pura")
        String desc,

        @Schema(description = "Novo preço de venda", example = "89.90")
        @Positive(message = "Preço deve ser positivo")
        BigDecimal preco,

        @Schema(description = "Nova quantidade disponível em estoque", example = "40")
        @PositiveOrZero(message = "Quantidade não pode ser negativa")
        Integer qtd,

        @Schema(description = "Nova quantidade mínima para alerta de estoque", example = "10")
        @PositiveOrZero(message = "Quantidade mínima não pode ser negativa")
        Integer qtd_min
){}