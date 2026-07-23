package com.wictor.dto.pagamento;

import com.wictor.enums.FormaPagamento;
import com.wictor.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados retornados de um pagamento")
public record PagamentoResponseDto(

        @Schema(description = "Identificador do pagamento", example = "1")
        Integer id,

        @Schema(description = "Nome do plano contratado", example = "Plano Premium")
        String plano,

        @Schema(description = "Valor do pagamento", example = "99.90")
        BigDecimal valor,

        @Schema(description = "Forma de pagamento utilizada", example = "PIX")
        FormaPagamento formaPagamento,

        @Schema(description = "Status do pagamento", example = "PAGO")
        StatusPagamento status,

        @Schema(description = "Data e hora do pagamento", example = "2026-07-22T14:30:00")
        LocalDateTime data
){}