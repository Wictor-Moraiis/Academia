package com.wictor.dto.aluno;

import com.wictor.dto.user.UserResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlunoResponseDto(
        UserResponseDto user,
        BigDecimal altura,
        BigDecimal peso,
        String objetivo,
        String planoNome,
        LocalDate planoVencimento,
        boolean vencido
) {}