package com.wictor.dto.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroResponseDto(

        Integer id,
        String nome,
        LocalDate data,
        BigDecimal valor
) {}
