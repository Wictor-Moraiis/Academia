package com.wictor.dto.financeiro;

import com.wictor.enums.TipoFinanceiro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para atualização de um lançamento financeiro")
public record FinanceiroUpdateDto(

        @Schema(description = "Novo nome ou descrição da movimentação",
                example = "Pagamento de energia elétrica")
        String nome,

        @Schema(description = "Novo tipo da movimentação financeira", example = "DESPESA")
        TipoFinanceiro tipo,

        @Schema(description = "Nova data da movimentação", example = "2026-07-25")
        LocalDate data,

        @Schema(description = "Novo valor da movimentação", example = "320.50")
        BigDecimal valor
){}