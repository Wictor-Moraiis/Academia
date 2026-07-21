package com.wictor.integration.abacate;

import com.wictor.integration.abacate.dto.*;
import com.wictor.model.Aluno;
import com.wictor.model.Plano;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbacateCheckoutService {

    private final AbacateClient client;

    public CheckoutResponse criarCheckoutPlano(Plano plano, Aluno aluno, String externalId) {

        if (aluno.getAbacateCustomerId() == null) {

            throw new IllegalStateException("Aluno não possui customer cadastrado na AbacatePay.");
        }

        CheckoutRequest request = new CheckoutRequest(

                List.of(new CheckoutRequest.Item(plano.getAbacateProductId(), 1)),

                aluno.getAbacateCustomerId(),

                List.of("PIX", "CARD"),

                "http://localhost:3000/pagamento/sucesso",

                "http://localhost:3000/pagamento/finalizado",

                externalId
        );

        return client.criarCheckout(request);
    }

    public SubscriptionResponse criarSubscriptionPlano(Plano plano, Aluno aluno,  String externalId) {

        if (aluno.getAbacateCustomerId() == null) {

            throw new IllegalStateException("Aluno não possui customer cadastrado na AbacatePay.");
        }

        SubscriptionRequest request = new SubscriptionRequest(

                List.of(new SubscriptionRequest.Item(plano.getAbacateProductId(), 1)),

                aluno.getAbacateCustomerId(),

                List.of("CARD"),

                "http://localhost:3000/pagamento/sucesso",

                "http://localhost:3000/pagamento/finalizado",

                externalId
        );

        return client.criarSubscription(request);
    }

    public void cancelarAssinatura(String subscriptionId) {

        CancelarSubscriptionResponse response = client.cancelarAssinatura(subscriptionId);

        if (!response.success()) {

            throw new RuntimeException(response.error());
        }
    }

    public ChangePlanoResponse alterarPlano(String subscriptionId, Plano plano) {

        ChangePlanoResponse response = client.alterarPlano
                (new ChangePlanoRequest(subscriptionId, plano.getAbacateProductId(), 1));

        if (!response.success()) {

            throw new RuntimeException(response.error());
        }

        return response;
    }
}