package com.wictor.dto.categoria;

import com.wictor.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados necessários para cadastrar uma categoria de funcionário")
public record CategoriaDto(

        @Schema(description = "Nome da categoria", example = "Professor")
        @NotBlank(message = "Nome da categoria é obrigatório")
        String nome,

        @Schema(description = "Salário base da categoria", example = "3500.00")
        @NotNull(message = "Salário da categoria é obrigatório")
        @Positive(message = "Salário da categoria deve ser positivo")
        BigDecimal salario,

        @Schema(description = "Cargo associado à categoria", example = "PROFESSOR")
        Role role
){}