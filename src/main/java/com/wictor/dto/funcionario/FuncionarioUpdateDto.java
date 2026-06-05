package com.wictor.dto.funcionario;

import com.wictor.enums.TipoContrato;
import java.time.LocalTime;

public record FuncionarioUpdateDto(

        String cref,
        TipoContrato tipoContrato,
        LocalTime turnoIni,
        LocalTime turnoFim,
        String banco,
        String agencia,
        String conta,
        String tipoConta,
        Integer categoriaId
) {
}