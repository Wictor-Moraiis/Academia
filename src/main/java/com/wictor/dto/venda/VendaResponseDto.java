package com.wictor.dto.venda;

import com.wictor.dto.itemfinanceiro.ItemFinanceiroResponseDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VendaResponseDto(

        Integer financeiroId,
        String funcionarioNome,
        BigDecimal total,
        LocalDate data,
        List<ItemFinanceiroResponseDto> itens
){}
