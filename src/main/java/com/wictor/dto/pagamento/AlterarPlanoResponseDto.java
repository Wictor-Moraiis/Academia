package com.wictor.dto.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resposta retornada após solicitar alteração de plano de uma assinatura")
public record AlterarPlanoResponseDto(

        @Schema(description = "Identificador da solicitação de alteração", example = "chg_123456789")
        String id,

        @Schema(description = "Identificador da assinatura na plataforma de pagamento",
                example = "sub_123456789")
        String subscriptionId,

        @Schema(description = "Status da solicitação", example = "PENDING")
        String status,

        @Schema(description = "Quantidade de assinaturas afetadas", example = "1")
        Integer quantity,

        @Schema(description = "Novo valor da assinatura após a alteração", example = "99.90")
        BigDecimal novoValor,

        @Schema(description = "Data e hora da solicitação", example = "2026-07-22T14:30:00")
        LocalDateTime solicitadoEm
){}