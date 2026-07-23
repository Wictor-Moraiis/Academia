package com.wictor.dto.financeiro;

import com.wictor.enums.OrigemFinanceiro;
import com.wictor.enums.TipoFinanceiro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados retornados de um lançamento financeiro")
public record FinanceiroResponseDto(

        @Schema(description = "Identificador do lançamento", example = "15")
        Integer id,

        @Schema(description = "Nome ou descrição da movimentação",
                example = "Compra de materiais de limpeza")
        String nome,

        @Schema(description = "Tipo da movimentação financeira", example = "DESPESA")
        TipoFinanceiro tipo,

        @Schema(description = "Origem do lançamento financeiro", example = "MANUAL")
        OrigemFinanceiro origem,

        @Schema(description = "Data da movimentação", example = "2026-07-22")
        LocalDate data,

        @Schema(description = "Valor da movimentação", example = "250.00")
        BigDecimal valor,

        @Schema(description = "Funcionário responsável pelo lançamento",
                example = "João Silva")
        String funcionario
){}