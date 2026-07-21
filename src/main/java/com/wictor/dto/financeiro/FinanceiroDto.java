package com.wictor.dto.financeiro;

import com.wictor.enums.TipoFinanceiro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceiroDto(

        @NotBlank(message = "Nome da atividade financeira é obrigatório")
        String nome,

        @NotNull(message = "Tipo da atividade financeira é obrigatório")
        TipoFinanceiro tipo,

        @NotNull(message = "Data da atividade financeira é obrigatório")
        LocalDate data,

        @NotNull(message = "Valor da atividade financeira é obrigatório")
        BigDecimal valor
){}
