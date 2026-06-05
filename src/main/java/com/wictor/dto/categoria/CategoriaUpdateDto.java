package com.wictor.dto.categoria;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CategoriaUpdateDto(

    String nome,
    @Positive(message = "Salário da categoria  deve ser positivo")
    BigDecimal salario
) {}
