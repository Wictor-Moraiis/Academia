package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Forma de pagamento utilizada na transação.")
public enum FormaPagamento {

    @Schema(description = "Pagamento realizado via PIX.")
    PIX,

    @Schema(description = "Pagamento realizado por cartão de crédito ou débito.")
    CARTAO,

    @Schema(description = "Pagamento realizado em dinheiro.")
    DINHEIRO
}