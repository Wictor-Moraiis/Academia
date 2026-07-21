package com.wictor.enums;

import java.time.LocalDate;

public enum CicloPlano {

    AVULSO,
    MENSAL,
    SEMESTRAL,
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

