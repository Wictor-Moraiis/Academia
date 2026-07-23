package com.wictor.dto.funcionario;

import com.wictor.dto.user.UserDto;
import com.wictor.enums.TipoContaBancaria;
import com.wictor.enums.TipoContrato;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "Dados necessários para cadastrar um funcionário")
public record FuncionarioDto(

        @Schema(description = "Dados do usuário vinculado ao funcionário")
        @NotNull(message = "Dados do usuário são obrigatórios")
        @Valid
        UserDto user,

        @Schema(description = "Registro CREF do profissional, quando aplicável",
                example = "123456-G/SP")
        String cref,

        @Schema(description = "Tipo de contrato do funcionário", example = "CLT")
        @NotNull(message = "Tipo de contrato é obrigatório")
        TipoContrato tipoContrato,

        @Schema(description = "Horário de início do turno", example = "08:00")
        @NotNull(message = "Horário de início do turno é obrigatório")
        LocalTime turnoIni,

        @Schema(description = "Horário de término do turno", example = "17:00")
        @NotNull(message = "Horário de fim do turno é obrigatório")
        LocalTime turnoFim,

        @Schema(description = "Nome do banco", example = "Banco do Brasil")
        @NotBlank(message = "Nome do banco é obrigatório")
        String banco,

        @Schema(description = "Número da agência bancária", example = "1234")
        @NotBlank(message = "Agência é obrigatório")
        String agencia,

        @Schema(description = "Número da conta bancária", example = "12345-6")
        @NotBlank(message = "Conta é obrigatória")
        String conta,

        @Schema(description = "Tipo da conta bancária", example = "CORRENTE")
        @NotNull(message = "Tipo da conta é obrigatório")
        TipoContaBancaria tipoConta,

        @Schema(description = "ID da categoria do funcionário", example = "2")
        @NotNull(message = "Categoria é obrigatória")
        Integer categoriaId
){}