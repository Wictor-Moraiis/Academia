package com.wictor.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AlterarPlanoResponseDto(

        String id,
        String subscriptionId,
        String status,
        Integer quantity,
        BigDecimal novoValor,
        LocalDateTime solicitadoEm
){}
