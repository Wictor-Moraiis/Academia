package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Ciclo de cobrança e validade de um plano.")
public enum CicloPlano {

    @Schema(description = "Plano avulso com validade de 1 dia.")
    AVULSO,

    @Schema(description = "Plano com renovação mensal.")
    MENSAL,

    @Schema(description = "Plano com renovação a cada 6 meses.")
    SEMESTRAL,

    @Schema(description = "Plano com renovação anual.")
    ANUAL;

    public LocalDate calcularVencimento(LocalDate dataInicio) {

        return switch (this) {

            case AVULSO -> dataInicio.plusDays(1);

            case MENSAL -> dataInicio.plusMonths(1);

            case SEMESTRAL -> dataInicio.plusMonths(6);

            case ANUAL -> dataInicio.plusYears(1);
        };
    }
}