package com.wictor.integration.abacate.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WebhookRequest(

        String id,

        String event,

        Integer apiVersion,

        Boolean devMode,

        Data data
){

    public record Data(

            Checkout checkout,

            Subscription subscription,

            Customer customer,

            Payment payment,

            PayerInformation payerInformation,

            String changeSource,

            String pendingUpdateId,

            String productId,

            Integer quantity,

            Integer newAmount,

            String status,

            LocalDateTime requestedAt
    ){}

    public record Subscription(

            String id,

            Integer amount,

            String currency,

            String method,

            String status,

            String frequency
    ){}

    public record Customer(

            String id,

            String name,

            String email,

            String taxId
    ){}

    public record Payment(

            String id,

            String externalId,

            String status
    ){}

    public record PayerInformation(

            String method
    ){}

    public record Checkout(

            String id,

            String externalId,

            String status,

            String frequency,

            String customerId,

            List<String> methods,

            List<Item> items
    ){}

    public record Item(

            String id,

            Integer quantity
    ){}
}