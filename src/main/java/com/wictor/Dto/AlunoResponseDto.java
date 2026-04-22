package com.wictor.Dto;

import java.math.BigDecimal;

public record AlunoResponseDto(
        Integer id,
        String nome,
        String objetivo,
        BigDecimal altura,
        BigDecimal peso,
        String planoNome
) {}