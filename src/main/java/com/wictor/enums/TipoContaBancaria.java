package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de conta bancária utilizada para recebimentos e pagamentos.")
public enum TipoContaBancaria {

    @Schema(description = "Conta corrente destinada às movimentações financeiras do dia a dia.")
    CORRENTE,

    @Schema(description = "Conta poupança destinada à reserva de valores e rendimentos.")
    POUPANCA,

    @Schema(description = "Conta utilizada exclusivamente para recebimento de salário.")
    SALARIO,

    @Schema(description = "Conta de pagamento utilizada para transações financeiras.")
    PAGAMENTO,

    @Schema(description = "Conta compartilhada entre dois ou mais titulares.")
    CONJUNTA,

    @Schema(description = "Conta destinada à realização de investimentos financeiros.")
    INVESTIMENTO
}