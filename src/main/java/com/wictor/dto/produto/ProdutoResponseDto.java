package com.wictor.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados retornados de um produto")
public record ProdutoResponseDto(

        @Schema(description = "Identificador do produto", example = "1")
        Integer id,

        @Schema(description = "Nome do produto", example = "Whey Protein 900g")
        String nome,

        @Schema(description = "Descrição do produto",
                example = "Suplemento proteico sabor chocolate")
        String desc,

        @Schema(description = "Preço de venda", example = "129.90")
        BigDecimal preco,

        @Schema(description = "Quantidade disponível em estoque", example = "25")
        Integer qtd,

        @Schema(description = "Quantidade mínima em estoque", example = "5")
        Integer qtd_min,

        @Schema(description = "Nome do arquivo da foto do produto",
                example = "whey-protein.png")
        String foto,

        @Schema(description = "Indica se o produto está ativo", example = "true")
        boolean ativo
){}