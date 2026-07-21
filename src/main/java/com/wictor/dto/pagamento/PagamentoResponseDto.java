package com.wictor.dto.pagamento;

import com.wictor.enums.FormaPagamento;
import com.wictor.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDto(

        Integer id,
        String plano,
        BigDecimal valor,
        FormaPagamento formaPagamento,
        StatusPagamento status,
        LocalDateTime data
){}