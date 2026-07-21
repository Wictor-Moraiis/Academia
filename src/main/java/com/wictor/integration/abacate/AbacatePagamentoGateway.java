package com.wictor.integration.abacate;

import com.wictor.dto.pagamento.AlterarPlanoResponseDto;
import com.wictor.dto.pagamento.CheckoutPagamentoResponseDto;
import com.wictor.gateway.PagamentoGateway;
import com.wictor.integration.abacate.dto.ChangePlanoResponse;
import com.wictor.integration.abacate.dto.CheckoutResponse;
import com.wictor.integration.abacate.dto.SubscriptionResponse;
import com.wictor.model.Aluno;
import com.wictor.model.Plano;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AbacatePagamentoGateway implements PagamentoGateway {

    private final AbacateCheckoutService checkoutService;

    @Override
    public CheckoutPagamentoResponseDto criarCheckout(Plano plano, Aluno aluno, String externalId) {

        CheckoutResponse response = checkoutService.criarCheckoutPlano(plano, aluno, externalId);

        if (!response.success()){

            throw new RuntimeException(response.error());
        }

        return new CheckoutPagamentoResponseDto(
                response.data().id(),
                response.data().url(),
                response.data().status(),
                externalId,
                false
        );
    }

    @Override
    public CheckoutPagamentoResponseDto criarAssinatura(Plano plano, Aluno aluno, String externalId) {

        SubscriptionResponse response = checkoutService.criarSubscriptionPlano(plano, aluno, externalId);

        if (!response.success()) {

            throw new RuntimeException(response.error());
        }

        return new CheckoutPagamentoResponseDto(
                response.data().id(),
                response.data().url(),
                response.data().status(),
                externalId,
                true
        );
    }

    @Override
    public void cancelarAssinatura(String subscriptionId) {

        checkoutService.cancelarAssinatura(subscriptionId);
    }

    @Override
    public AlterarPlanoResponseDto alterarPlano(String subscriptionId, Plano plano) {

        ChangePlanoResponse response = checkoutService.alterarPlano(subscriptionId, plano);

        return new AlterarPlanoResponseDto(
                response.data().id(),
                response.data().subscriptionId(),
                response.data().status(),
                response.data().quantity(),
                BigDecimal.valueOf(response.data().newAmount(), 2),
                response.data().requestedAt()
        );
    }
}
