package com.wictor.dto.categoria;

import java.math.BigDecimal;

public record CategoriaResponseDto (
        Integer id,
        String nome,
        BigDecimal Salario
) {}
