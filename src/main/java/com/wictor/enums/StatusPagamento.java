package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status atual do pagamento.")
public enum StatusPagamento {

    @Schema(description = "Pagamento criado, aguardando confirmação.")
    PENDENTE,

    @Schema(description = "Pagamento confirmado com sucesso.")
    PAGO,

    @Schema(description = "Pagamento cancelado antes da conclusão.")
    CANCELADO,

    @Schema(description = "Falha no processamento do pagamento.")
    FALHOU
}