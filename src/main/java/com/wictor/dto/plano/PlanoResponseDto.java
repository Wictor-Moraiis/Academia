package com.wictor.dto.plano;

import java.math.BigDecimal;

public record PlanoResponseDto(
        Integer id,
        String nome,
        BigDecimal valor,
        Integer validade
) {}
