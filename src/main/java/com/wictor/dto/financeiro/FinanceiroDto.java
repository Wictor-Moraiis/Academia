package com.wictor.dto.financeiro;

import com.wictor.enums.TipoFinanceiro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados necessários para cadastrar um lançamento financeiro")
public record FinanceiroDto(

        @Schema(description = "Nome ou descrição da movimentação financeira",
                example = "Compra de materiais de limpeza")
        @NotBlank(message = "Nome da atividade financeira é obrigatório")
        String nome,

        @Schema(description = "Tipo da movimentação financeira", example = "DESPESA")
        @NotNull(message = "Tipo da atividade financeira é obrigatório")
        TipoFinanceiro tipo,

        @Schema(description = "Data da movimentação financeira", example = "2026-07-22")
        @NotNull(message = "Data da atividade financeira é obrigatório")
        LocalDate data,

        @Schema(description = "Valor da movimentação financeira", example = "250.00")
        @NotNull(message = "Valor da atividade financeira é obrigatório")
        BigDecimal valor
){}