package com.wictor.dto.pagamento;

import com.wictor.enums.FormaPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoPresencialDto(

        @NotNull(message = "Id do aluno é obrigatório")
        Integer alunoId,

        @NotNull(message = "Id do plano é obrigatório")
        Integer planoId,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento
){}
