package com.wictor.dto.financeiro;

import com.wictor.enums.OrigemFinanceiro;
import com.wictor.enums.TipoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroResponseDto(

        Integer id,
        String nome,
        TipoFinanceiro tipo,
        OrigemFinanceiro origem,
        LocalDate data,
        BigDecimal valor,
        String funcionario
){}
