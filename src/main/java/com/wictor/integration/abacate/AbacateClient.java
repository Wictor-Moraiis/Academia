package com.wictor.integration.abacate;

import com.wictor.integration.abacate.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AbacateClient{

    private final RestClient abacateRestClient;

    public ProductResponse criarProduto(ProductRequest request) {

        return abacateRestClient.post()
                .uri("/products/create")
                .body(request)
                .retrieve()
                .body(ProductResponse.class);
    }

    public CheckoutResponse criarCheckout(CheckoutRequest request) {

        return abacateRestClient.post()
                .uri("/checkouts/create")
                .body(request)
                .retrieve()
                .body(CheckoutResponse.class);
    }

    public SubscriptionResponse criarSubscription(SubscriptionRequest request) {

        return abacateRestClient.post()
                .uri("/subscriptions/create")
                .body(request)
                .retrieve()
                .body(SubscriptionResponse.class);
    }

    public CustomerResponse criarCustomer(CustomerRequest request) {

        return abacateRestClient.post()
                .uri("/customers/create")
                .body(request)
                .retrieve()
                .body(CustomerResponse.class);
    }

    public CancelarSubscriptionResponse cancelarAssinatura(String subscriptionId) {

        return abacateRestClient.post()
                .uri("/subscriptions/cancel")
                .body(new CancelarSubscriptionRequest(subscriptionId))
                .retrieve()
                .body(CancelarSubscriptionResponse.class);
    }

    public ChangePlanoResponse alterarPlano(ChangePlanoRequest request) {

        return abacateRestClient.post()
                .uri("/subscriptions/change-plan")
                .body(request)
                .retrieve()
                .body(ChangePlanoResponse.class);
    }

}