package com.wictor.dto.plano;

import com.wictor.enums.CicloPlano;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlanoUpdateDto(

        String nome,

        @Positive(message = "Valor do plano deve ser positivo")
        BigDecimal valor,

        CicloPlano ciclo,

        Boolean recorrente,

        Boolean ativo
){}

