package com.wictor.dto.plano;

import com.wictor.enums.CicloPlano;

import java.math.BigDecimal;

public record PlanoResponseDto(

        Integer id,
        String nome,
        BigDecimal valor,
        CicloPlano ciclo,
        Boolean recorrente
){}
