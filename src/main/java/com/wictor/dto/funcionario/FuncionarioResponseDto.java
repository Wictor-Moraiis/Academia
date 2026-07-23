package com.wictor.dto.funcionario;

import com.wictor.dto.user.UserResponseDto;
import com.wictor.enums.TipoContrato;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalTime;

@Schema(description = "Dados retornados de um funcionário")
public record FuncionarioResponseDto(

        @Schema(description = "Dados do usuário")
        UserResponseDto user,

        @Schema(description = "Registro CREF do profissional", example = "123456-G/SP")
        String cref,

        @Schema(description = "Tipo de contrato", example = "CLT")
        TipoContrato tipoContrato,

        @Schema(description = "Horário de início do turno", example = "08:00")
        LocalTime turnoIni,

        @Schema(description = "Horário de término do turno", example = "17:00")
        LocalTime turnoFim,

        @Schema(description = "Nome da categoria do funcionário", example = "Professor")
        String categoriaNome,

        @Schema(description = "Salário da categoria", example = "3500.00")
        BigDecimal categoriaSalario
){}