package com.wictor.integration.abacate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductRequest(

        String externalId,

        String name,

        Integer price,

        String currency,

        String description,

        String cycle
){}