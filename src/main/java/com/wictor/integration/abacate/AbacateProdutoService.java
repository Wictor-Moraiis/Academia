package com.wictor.integration.abacate;

import com.wictor.integration.abacate.dto.ProductRequest;
import com.wictor.integration.abacate.dto.ProductResponse;
import com.wictor.model.Plano;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AbacateProdutoService {

    private final AbacateClient client;

    public String criarProdutoPlano(Plano plano) {

        ProductRequest request = new ProductRequest(
                "plano-" + plano.getId(),
                plano.getNome(),
                plano.getValor().multiply(new BigDecimal(100)).intValue(),
                "BRL",
                "Plano academia", getCycle(plano)
        );

        ProductResponse response = client.criarProduto(request);

        if(!response.success()) {

            throw new RuntimeException("Erro ao criar produto AbacatePay: " + response.error());
        }

        return response.data().id();
    }

    private String getCycle(Plano plano) {

        if(!plano.isRecorrente()) {return null;}

        return switch(plano.getCiclo()) {
            case MENSAL -> "MONTHLY";
            case SEMESTRAL -> "SEMIANNUALLY";
            case ANUAL -> "ANNUALLY";
            case AVULSO -> null;
        };
    }
}