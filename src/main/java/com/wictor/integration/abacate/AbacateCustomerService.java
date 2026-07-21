package com.wictor.integration.abacate;

import com.wictor.integration.abacate.dto.CustomerRequest;
import com.wictor.integration.abacate.dto.CustomerResponse;
import com.wictor.model.Aluno;
import com.wictor.service.CpfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbacateCustomerService {

    private final AbacateClient abacateClient;

    public String criarCustomer(Aluno aluno) {

        String cpf = CpfService.Descriptografia(aluno.getUser().getCpf());

        CustomerRequest request = new CustomerRequest(

                aluno.getUser().getNome(),

                aluno.getUser().getEmail1(),

                aluno.getUser().getTel1(),

                cpf
        );

        CustomerResponse response = abacateClient.criarCustomer(request);

        if(!response.success()) {

            throw new RuntimeException(response.error());
        }

        return response.data().id();
    }
}