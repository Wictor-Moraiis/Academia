package com.wictor.integration.abacate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Payload recebido através do webhook da AbacatePay")
public record WebhookRequest(

        @Schema(description = "Identificador do evento recebido",
                example = "evt_123456789")
        String id,

        @Schema(description = "Tipo do evento enviado pela plataforma",
                example = "checkout.completed")
        String event,

        @Schema(description = "Versão da API do webhook", example = "2")
        Integer apiVersion,

        @Schema(description = "Indica se o evento foi gerado em ambiente de testes",
                example = "false")
        Boolean devMode,

        @Schema(description = "Dados relacionados ao evento recebido")
        Data data
){

    @Schema(description = "Dados detalhados do evento")
    public record Data(

            @Schema(description = "Dados do checkout")
            Checkout checkout,

            @Schema(description = "Dados da assinatura")
            Subscription subscription,

            @Schema(description = "Dados do cliente")
            Customer customer,

            @Schema(description = "Dados do pagamento")
            Payment payment,

            @Schema(description = "Informações do pagador")
            PayerInformation payerInformation,

            @Schema(description = "Origem da alteração realizada", example = "API")
            String changeSource,

            @Schema(description = "Identificador da alteração pendente", example = "upd_123456")
            String pendingUpdateId,

            @Schema(description = "Identificador do produto relacionado", example = "prod_123456")
            String productId,

            @Schema(description = "Quantidade relacionada ao evento", example = "1")
            Integer quantity,

            @Schema(description = "Novo valor aplicado", example = "9990")
            Integer newAmount,

            @Schema(description = "Status atual do evento", example = "completed")
            String status,

            @Schema(description = "Data e hora da solicitação", example = "2026-07-22T15:30:00")
            LocalDateTime requestedAt
    ){}

    @Schema(description = "Dados da assinatura")
    public record Subscription(

            @Schema(description = "Identificador da assinatura", example = "sub_123456")
            String id,

            @Schema(description = "Valor da assinatura", example = "9990")
            Integer amount,

            @Schema(description = "Moeda utilizada", example = "BRL")
            String currency,

            @Schema(description = "Método de pagamento", example = "CARD")
            String method,

            @Schema(description = "Status da assinatura", example = "active")
            String status,

            @Schema(description = "Frequência da cobrança", example = "monthly")
            String frequency
    ){}

    @Schema(description = "Dados do cliente")
    public record Customer(

            @Schema(description = "Identificador do cliente", example = "cus_123456")
            String id,

            @Schema(description = "Nome do cliente", example = "João Silva")
            String name,

            @Schema(description = "E-mail do cliente", example = "joao@email.com")
            String email,

            @Schema(description = "Documento fiscal do cliente", example = "12345678901")

            String taxId
    ){}

    @Schema(description = "Dados do pagamento")
    public record Payment(

            @Schema(description = "Identificador do pagamento", example = "pay_123456")
            String id,

            @Schema(description = "Identificador externo do pagamento",
                    example = "550e8400-e29b-41d4-a716-446655440000")
            String externalId,

            @Schema(description = "Status do pagamento", example = "PAID")
            String status
    ){}

    @Schema(description = "Informações do método utilizado pelo pagador")
    public record PayerInformation(

            @Schema(description = "Método de pagamento utilizado", example = "PIX")
            String method
    ){}

    @Schema(description = "Dados do checkout")
    public record Checkout(

            @Schema(description = "Identificador do checkout", example = "chk_123456")
            String id,

            @Schema(description = "Identificador externo do checkout", example = "checkout-001")
            String externalId,

            @Schema(description = "Status do checkout", example = "completed")
            String status,

            @Schema(description = "Frequência do checkout", example = "monthly")
            String frequency,

            @Schema(description = "Identificador do cliente", example = "cus_123456")
            String customerId,

            @Schema(description = "Métodos de pagamento disponíveis")
            List<String> methods,

            @Schema(description = "Itens do checkout")
            List<Item> items
    ){}

    @Schema(description = "Item relacionado ao checkout")
    public record Item(

            @Schema(description = "Identificador do item", example = "prod_123456")
            String id,

            @Schema(description = "Quantidade do item", example = "1")
            Integer quantity
    ){}
}