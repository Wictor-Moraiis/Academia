package com.wictor.integration.abacate.dto;

public record SubscriptionResponse(

        Data data,
        String error,
        boolean success
){
    public record Data(

            String id,
            String url,
            String status,
            String externalId
    ){}
}