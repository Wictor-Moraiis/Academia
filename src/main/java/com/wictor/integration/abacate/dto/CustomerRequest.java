package com.wictor.integration.abacate.dto;

public record CustomerRequest(

        String name,

        String email,

        String cellphone,

        String taxId
){}