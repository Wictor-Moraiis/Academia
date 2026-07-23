package com.wictor.dto.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados ao gerar um checkout de pagamento")
public record CheckoutPagamentoResponseDto(

        @Schema(description = "Identificador do checkout", example = "chk_123456789")
        String id,

        @Schema(description = "URL para realização do pagamento",
                example = "https://checkout.abacatepay.com/abc123")
        String url,

        @Schema(description = "Status atual do checkout", example = "PENDING")
        String status,

        @Schema(description = "Identificador externo gerado pelo sistema",
                example = "550e8400-e29b-41d4-a716-446655440000")
        String externalId,

        @Schema(description = "Indica se o pagamento é recorrente", example = "true")
        boolean recorrente
){}