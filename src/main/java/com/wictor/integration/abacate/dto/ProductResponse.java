package com.wictor.integration.abacate.dto;

public record ProductResponse(

        Data data,
        String error,
        boolean success
){

    public record Data(
            String id,
            String externalId,
            String name,
            Integer price,
            String currency,
            String status
    ){}
}