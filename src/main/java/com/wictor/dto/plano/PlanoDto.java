package com.wictor.dto.plano;

import com.wictor.enums.CicloPlano;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PlanoDto(

        @NotBlank(message = "Nome do plano é obrigatório")
        String nome,

        @NotNull(message = "Valor do plano é obrigatório")
        @Positive(message = "Valor do plano deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "Ciclo do plano é obrigatório")
        CicloPlano ciclo,

        @NotNull(message = "Recorrencia do plano é obrigatória")
        Boolean recorrente
){}
