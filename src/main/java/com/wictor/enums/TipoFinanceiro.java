package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Tipo de movimentação financeira registrada no sistema.")
public enum TipoFinanceiro {

    @Schema(description = "Receita proveniente da venda de produtos.")
    VENDA("Venda", true),

    @Schema(description = "Receita proveniente do pagamento de mensalidades.")
    MENSALIDADE("Mensalidade", true),

    @Schema(description = "Saída financeira referente a pagamentos realizados.")
    PAGAMENTO("Pagamento", false),

    @Schema(description = "Receita não recorrente ou não relacionada a vendas e mensalidades.")
    RECEITA_EXTRA("Receita Extra", true),

    @Schema(description = "Despesa geral da academia.")
    DESPESA("Despesa", false),

    @Schema(description = "Saída financeira destinada ao pagamento de salários.")
    SALARIO("Salário", false),

    @Schema(description = "Despesa referente à manutenção de equipamentos ou instalações.")
    MANUTENCAO("Manutenção", false);

    private final String descricao;
    private final boolean entrada;

    TipoFinanceiro(String descricao, boolean entrada) {
        this.descricao = descricao;
        this.entrada = entrada;
    }

    public boolean isSaida() {
        return !entrada;
    }
}