package com.wictor.integration.abacate.dto;

import java.util.List;

public record SubscriptionRequest(

        List<Item> items,

        String customerId,

        List<String> methods,

        String returnUrl,

        String completionUrl,

        String externalId
){

    public record Item(

            String id,

            Integer quantity
    ){}
}