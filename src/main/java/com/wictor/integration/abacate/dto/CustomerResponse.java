package com.wictor.integration.abacate.dto;

public record CustomerResponse(

        Data data,
        String error,
        boolean success
){

    public record Data(

            String id,
            String name,
            String email
    ){}
}
