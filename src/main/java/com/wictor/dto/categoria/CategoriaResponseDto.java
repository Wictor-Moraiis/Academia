package com.wictor.dto.categoria;

import com.wictor.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados retornados de uma categoria")
public record CategoriaResponseDto(

        @Schema(description = "Identificador da categoria", example = "1")
        Integer id,

        @Schema(description = "Nome da categoria", example = "Professor")
        String nome,

        @Schema(description = "Salário base da categoria", example = "3500.00")
        BigDecimal salario,

        @Schema(description = "Cargo associado à categoria", example = "PROFESSOR")
        Role role
){}