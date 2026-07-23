package com.wictor.dto.categoria;

import com.wictor.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para atualização de uma categoria")
public record CategoriaUpdateDto(

        @Schema(description = "Novo nome da categoria", example = "Gerente")
        String nome,

        @Schema(description = "Novo salário base da categoria", example = "5500.00")
        @Positive(message = "Salário da categoria deve ser positivo")
        BigDecimal salario,

        @Schema(description = "Novo cargo associado à categoria", example = "GERENTE")
        Role role
){}