package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Origem que gerou o lançamento financeiro.")
public enum OrigemFinanceiro {

    @Schema(description = "Pagamento realizado presencialmente.")
    PAGAMENTO_PRESENCIAL("Pagamento presencial"),

    @Schema(description = "Pagamento realizado por meio da plataforma online.")
    PAGAMENTO_ONLINE("Pagamento online"),

    @Schema(description = "Renovação automática ou manual de uma assinatura.")
    RENOVACAO_ASSINATURA("Renovação assinatura"),

    @Schema(description = "Venda de um produto.")
    VENDA_PRODUTO("Venda de produto"),

    @Schema(description = "Lançamento financeiro registrado manualmente por um usuário.")
    LANCAMENTO_MANUAL("Lançamento manual"),

    @Schema(description = "Lançamento financeiro gerado automaticamente pelo sistema.")
    AUTOMATICO("Automático");

    private final String descricao;

    OrigemFinanceiro(String descricao) {
        this.descricao = descricao;
    }
}