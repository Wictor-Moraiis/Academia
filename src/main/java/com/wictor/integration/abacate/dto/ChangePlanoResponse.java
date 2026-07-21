package com.wictor.integration.abacate.dto;

import java.time.LocalDateTime;

public record ChangePlanoResponse(

        Data data,
        Boolean success,
        String error
){

    public record Data(

            String id,
            String subscriptionId,
            String status,
            String productId,
            Integer quantity,
            Integer newAmount,
            LocalDateTime requestedAt
    ){}
}
