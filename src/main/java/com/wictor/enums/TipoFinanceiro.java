package com.wictor.enums;

import lombok.Getter;

@Getter
public enum TipoFinanceiro {

    VENDA("Venda", true),
    MENSALIDADE("Mensalidade", true),
    PAGAMENTO("Pagamento", false),
    RECEITA_EXTRA("Receita Extra", true),
    DESPESA("Despesa", false),
    SALARIO("Salário", false),
    MANUTENCAO("Manutenção", false);

    private final String descricao;
    private final boolean entrada;

    TipoFinanceiro(String descricao, boolean entrada) {
        this.descricao = descricao;
        this.entrada = entrada;
    }

    public boolean isSaida(){return !entrada;}
}