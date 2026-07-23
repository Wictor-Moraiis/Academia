package com.wictor.dto.log;

import com.wictor.enums.AcaoLog;
import com.wictor.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Representa um registro de auditoria utilizado internamente pelo sistema")
public record LogDto(

        @Schema(description = "Usuário responsável pela ação")
        User usuario,

        @Schema(description = "Ação executada", example = "CRIAR")
        AcaoLog acao,

        @Schema(description = "Entidade afetada", example = "Aluno")
        String entidade,

        @Schema(description = "Identificador da entidade", example = "15")
        Serializable entidadeId,

        @Schema(description = "Descrição da ação realizada", example = "Aluno cadastrado com sucesso")
        String descricao,

        @Schema(description = "Indica se a operação foi concluída com sucesso", example = "true")
        Boolean sucesso
){}