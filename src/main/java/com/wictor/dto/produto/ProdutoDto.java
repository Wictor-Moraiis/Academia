package com.wictor.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados necessários para cadastrar um produto")
public record ProdutoDto(

        @Schema(description = "Nome do produto", example = "Whey Protein 900g")
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Schema(description = "Descrição do produto",
                example = "Suplemento proteico sabor chocolate")
        String desc,

        @Schema(description = "Preço de venda do produto", example = "129.90")
        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        BigDecimal preco,

        @Schema(description = "Quantidade disponível em estoque", example = "25")
        @NotNull(message = "Quantidade é obrigatória")
        @PositiveOrZero(message = "Quantidade não pode ser negativa")
        Integer qtd,

        @Schema(description = "Quantidade mínima para alerta de estoque", example = "5")
        @NotNull(message = "Quantidade mínima é obrigatória")
        @PositiveOrZero(message = "Quantidade mínima não pode ser negativa")
        Integer qtd_min
){}