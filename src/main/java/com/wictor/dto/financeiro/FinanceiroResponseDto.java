package com.wictor.dto.financeiro;

import com.wictor.enums.TipoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroResponseDto(

        Integer id,
        String nome,
        TipoFinanceiro tipo,
        LocalDate data,
        BigDecimal valor
) {}
