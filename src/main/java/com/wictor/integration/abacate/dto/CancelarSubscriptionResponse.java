package com.wictor.integration.abacate.dto;

public record CancelarSubscriptionResponse(

        Data data,
        Boolean success,
        String error
){

    public record Data(

            String id,
            String customerId,
            Integer amount,
            String status,
            String method
    ){}
}