package com.wictor.dto.produto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoDto(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String desc,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        BigDecimal preco,

        @NotNull(message = "Quantidade é obrigatória")
        @PositiveOrZero(message = "Quantidade não pode ser negativa")
        Integer qtd,

        @NotNull(message = "Quantidade mínima é obrigatória")
        @PositiveOrZero(message = "Quantidade mínima não pode ser negativa")
        Integer qtd_min
){}
