package com.wictor.dto.aluno;

import com.wictor.enums.ObjetivoTreino;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para atualização de um aluno")
public record AlunoUpdateDto(

        @Schema(description = "Informações de saúde relevantes para os treinos",
                example = "Alergia a anti-inflamatórios")
        String saude,

        @Schema(description = "Observações adicionais",
                example = "Treina apenas aos finais de semana")
        String obs,

        @Schema(description = "Nova altura do aluno em metros", example = "1.80")
        @Positive(message = "Altura do aluno deve ser positivo")
        BigDecimal altura,

        @Schema(description = "Novo peso do aluno em quilogramas", example = "80.2")
        @Positive(message = "Peso do aluno deve ser positivo")
        BigDecimal peso,

        @Schema(description = "Novo objetivo de treinamento", example = "EMAGRECIMENTO")
        ObjetivoTreino objetivo,

        @Schema(description = "ID do novo plano", example = "2")
        Integer planoId,

        @Schema(description = "Nova data de vencimento do plano", example = "2026-12-31")
        LocalDate vencimento,

        @Schema(description = "Indica se o plano está vencido", example = "false")
        Boolean vencido
){}