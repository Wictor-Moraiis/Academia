package com.wictor.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroUpdateDto(

        String nome,
        LocalDate data,
        BigDecimal valor
) {}
