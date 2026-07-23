package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ação realizada que originou um registro de auditoria.")
public enum AcaoLog {

    @Schema(description = "Cadastro de um novo registro.")
    CADASTRO,

    @Schema(description = "Alteração de um registro existente.")
    ALTERACAO,

    @Schema(description = "Exclusão de um registro.")
    EXCLUSAO,

    @Schema(description = "Consulta de informações.")
    CONSULTA,

    @Schema(description = "Autenticação do usuário no sistema.")
    LOGIN,

    @Schema(description = "Encerramento da sessão do usuário.")
    LOGOUT,

    @Schema(description = "Registro de uma venda.")
    VENDA,

    @Schema(description = "Geração de um relatório.")
    RELATORIO,

    @Schema(description = "Outras ações não categorizadas.")
    OUTRO
}