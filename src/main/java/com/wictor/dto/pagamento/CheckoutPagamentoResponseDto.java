package com.wictor.dto.pagamento;

public record CheckoutPagamentoResponseDto(

        String id,
        String url,
        String status,
        String externalId,
        boolean recorrente
){}