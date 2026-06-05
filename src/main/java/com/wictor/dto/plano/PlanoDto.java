package com.wictor.dto.plano;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PlanoDto(

        @NotBlank(message = "Nome do plano é obrigatório")
        String nome,

        @NotNull(message = "Valor do plano é obrigatório")
        @Positive(message = "Valor do plano deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "Validade do plano é obrigatória")
        @Positive(message = "A Validade do plano deve ser positiva")
        Integer validade
) {}
