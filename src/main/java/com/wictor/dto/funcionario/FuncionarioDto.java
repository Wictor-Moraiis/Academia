package com.wictor.dto.funcionario;

import com.wictor.dto.user.UserDto;
import com.wictor.enums.TipoContrato;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record FuncionarioDto(

        @NotNull(message = "Dados do usuário são obrigatórios")
        @Valid
        UserDto user,

        String cref,
        @NotNull(message = "Tipo de contrato é obrigatório")
        TipoContrato tipoContrato,
        @NotNull(message = "Horário de início do turno é obrigatório")
        LocalTime turnoIni,
        @NotNull(message = "Horário de fim do turno é obrigatório")
        LocalTime turnoFim,
        @NotBlank(message = "Nome do banco é obrigatório")
        String banco,
        @NotBlank(message = "Agência é obrigatório")
        String agencia,
        @NotBlank(message = "Conta é obrigatória")
        String conta,
        @NotNull(message = "Tipo da conta é obrigatório")
        String tipoConta,
        @NotNull(message = "Categoria é obrigatória")
        Integer categoriaId
) { }