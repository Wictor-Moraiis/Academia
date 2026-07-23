package com.wictor.dto.venda;

import com.wictor.dto.itemfinanceiro.ItemFinanceiroResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dados retornados de uma venda")
public record VendaResponseDto(

        @Schema(description = "Identificador do lançamento financeiro da venda",
                example = "25")
        Integer financeiroId,

        @Schema(description = "Nome do funcionário responsável pela venda",
                example = "João Silva")
        String funcionarioNome,

        @Schema(description = "Valor total da venda", example = "259.80")
        BigDecimal total,

        @Schema(description = "Data da venda", example = "2026-07-22")
        LocalDate data,

        @Schema(description = "Itens que compõem a venda")
        List<ItemFinanceiroResponseDto> itens
){}