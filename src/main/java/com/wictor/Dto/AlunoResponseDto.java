package com.wictor.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlunoResponseDto(
        Integer id,
        String nome,
        BigDecimal altura,
        BigDecimal peso,
        String objetivo,
        String planoNome,
        LocalDate planoVencimento,
        boolean vencido
) {}