package com.wictor.dto.log;

import com.wictor.enums.AcaoLog;
import com.wictor.model.User;

public record LogDto(

        User usuario,

        AcaoLog acao,

        String entidade,

        Integer entidadeId,

        String descricao,

        Boolean sucesso
) {}