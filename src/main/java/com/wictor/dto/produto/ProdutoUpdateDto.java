package com.wictor.dto.produto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoUpdateDto(

        String nome,

        String desc,

        @Positive(message = "Preço deve ser positivo")
        BigDecimal preco,

        @PositiveOrZero(message = "Quantidade não pode ser negativa")
        Integer qtd,

        @PositiveOrZero(message = "Quantidade mínima não pode ser negativa")
        Integer qtd_min
){}
