package com.wictor.dto.plano;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlanoUpdateDto(

        String nome,

        @Positive(message = "Valor do plano deve ser positivo")
        BigDecimal valor,

        @Positive(message = "A Validade do plano deve ser positiva")
        Integer validade
) {}

