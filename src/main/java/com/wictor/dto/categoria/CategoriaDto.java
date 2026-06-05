package com.wictor.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CategoriaDto (

    @NotBlank(message = "Nome da categoria é obrigatório")
    String nome,

    @NotNull(message = "Salário da categoria  é obrigatório")
    @Positive(message = "Salário da categoria  deve ser positivo")
    BigDecimal salario
) {}
