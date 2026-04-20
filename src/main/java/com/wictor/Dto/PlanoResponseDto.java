package com.wictor.Dto;

import java.math.BigDecimal;

public record PlanoResponseDto(
        Integer id,
        String nome,
        BigDecimal valor,
        Integer validade
) {}
