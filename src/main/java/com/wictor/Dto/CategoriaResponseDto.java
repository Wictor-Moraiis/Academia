package com.wictor.Dto;

import java.math.BigDecimal;

public record CategoriaResponseDto (
        Integer id,
        String nome,
        BigDecimal Salario
) {}
