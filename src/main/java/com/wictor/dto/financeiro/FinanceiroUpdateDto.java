package com.wictor.dto.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroUpdateDto(

        String nome,
        LocalDate data,
        BigDecimal valor
) {}
