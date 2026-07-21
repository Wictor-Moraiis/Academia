package com.wictor.gateway;

import com.wictor.dto.pagamento.AlterarPlanoResponseDto;
import com.wictor.dto.pagamento.CheckoutPagamentoResponseDto;
import com.wictor.model.Aluno;
import com.wictor.model.Plano;

public interface PagamentoGateway {

    CheckoutPagamentoResponseDto criarCheckout(Plano plano, Aluno aluno, String externalId);

    CheckoutPagamentoResponseDto criarAssinatura(Plano plano, Aluno aluno, String externalId);

    void cancelarAssinatura(String subscriptionId);

    AlterarPlanoResponseDto alterarPlano(String subscriptionId, Plano plano);
}
