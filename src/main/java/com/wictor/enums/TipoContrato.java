package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de contrato de trabalho do funcionário.")
public enum TipoContrato {

    @Schema(description = "Contrato regido pela Consolidação das Leis do Trabalho (CLT).")
    CLT,

    @Schema(description = "Prestação de serviços como Pessoa Jurídica (PJ).")
    PJ
}