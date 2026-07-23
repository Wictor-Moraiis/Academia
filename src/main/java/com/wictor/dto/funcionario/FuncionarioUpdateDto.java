package com.wictor.dto.funcionario;

import com.wictor.enums.TipoContaBancaria;
import com.wictor.enums.TipoContrato;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "Dados para atualização de um funcionário")
public record FuncionarioUpdateDto(

        @Schema(description = "Novo registro CREF", example = "654321-G/SP")
        String cref,

        @Schema(description = "Novo tipo de contrato", example = "PJ")
        TipoContrato tipoContrato,

        @Schema(description = "Novo horário de início do turno", example = "09:00")
        LocalTime turnoIni,

        @Schema(description = "Novo horário de término do turno", example = "18:00")
        LocalTime turnoFim,

        @Schema(description = "Novo banco", example = "Itaú")
        String banco,

        @Schema(description = "Nova agência bancária", example = "4321")
        String agencia,

        @Schema(description = "Nova conta bancária", example = "65432-1")
        String conta,

        @Schema(description = "Novo tipo da conta bancária", example = "POUPANCA")
        TipoContaBancaria tipoConta,

        @Schema(description = "ID da nova categoria", example = "3")
        Integer categoriaId
){}