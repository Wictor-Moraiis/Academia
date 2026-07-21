package com.wictor.enums;

import lombok.Getter;

@Getter
public enum OrigemFinanceiro {

    PAGAMENTO_PRESENCIAL("Pagamento presencial"),
    PAGAMENTO_ONLINE("Pagamento online"),
    RENOVACAO_ASSINATURA("Renovação assinatura"),
    VENDA_PRODUTO("Venda de produto"),
    LANCAMENTO_MANUAL("Lançamento manual"),
    AUTOMATICO("Automático");

    private final String descricao;

    OrigemFinanceiro(String descricao) {this.descricao = descricao;}
}