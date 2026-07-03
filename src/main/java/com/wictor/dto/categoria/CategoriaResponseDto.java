package com.wictor.dto.categoria;

import com.wictor.enums.Role;

import java.math.BigDecimal;

public record CategoriaResponseDto (
        Integer id,
        String nome,
        BigDecimal Salario,
        Role role
) {}
