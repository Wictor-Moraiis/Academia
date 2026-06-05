package com.wictor.dto.funcionario;

import com.wictor.enums.TipoContrato;

import java.math.BigDecimal;
import java.time.LocalTime;

public record FuncionarioResponseDto (
       Integer id,
       String nome,
       String cref,
       TipoContrato tipoContrato,
       LocalTime turnoIni,
       LocalTime turnoFim,
       String categoriaNome,
       BigDecimal categoriaSalario)
{}