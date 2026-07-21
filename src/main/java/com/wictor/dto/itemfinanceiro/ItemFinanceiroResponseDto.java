package com.wictor.dto.itemfinanceiro;

import java.math.BigDecimal;

public record ItemFinanceiroResponseDto(

        String produtoNome,
        Integer qtd,
        BigDecimal valorUnitario,
        BigDecimal desconto,
        BigDecimal subtotal
){}
