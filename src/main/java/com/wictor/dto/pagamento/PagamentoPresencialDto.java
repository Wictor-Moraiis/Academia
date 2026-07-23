package com.wictor.dto.pagamento;

import com.wictor.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados necessários para registrar um pagamento presencial")
public record PagamentoPresencialDto(

        @Schema(description = "ID do aluno", example = "15")
        @NotNull(message = "Id do aluno é obrigatório")
        Integer alunoId,

        @Schema(description = "ID do plano contratado", example = "2")
        @NotNull(message = "Id do plano é obrigatório")
        Integer planoId,

        @Schema(description = "Forma de pagamento utilizada", example = "PIX")
        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento
){}