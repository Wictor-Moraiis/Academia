package com.wictor.dto.log;

import com.wictor.enums.AcaoLog;
import com.wictor.model.User;

import java.io.Serializable;

public record LogDto(

        User usuario,

        AcaoLog acao,

        String entidade,

        Serializable entidadeId,

        String descricao,

        Boolean sucesso
){}