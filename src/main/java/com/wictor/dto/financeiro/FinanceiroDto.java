package com.wictor.dto.financeiro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroDto(

        @NotBlank(message = "Nome da atividade financeira é obrigatório")
        String nome,

        @NotBlank(message = "Tipo da atividade financeira é obrigatório")
        String tipo,

        @NotNull(message = "Data da atividade financeira é obrigatório")
        LocalDate data,

        @NotNull(message = "Valor da atividade financeira é obrigatório")
        BigDecimal valor
) {}
