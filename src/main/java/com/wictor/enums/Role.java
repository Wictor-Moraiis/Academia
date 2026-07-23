package com.wictor.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Perfil de acesso do usuário no sistema.")
public enum Role {

    @Schema(description = "Administrador com acesso total ao sistema.")
    ADMIN,

    @Schema(description = "Gerente com acesso às funcionalidades administrativas e operacionais.")
    GERENTE,

    @Schema(description = "Recepcionista responsável pelo atendimento e operações de recepção.")
    RECEPCIONISTA,

    @Schema(description = "Professor responsável pelo acompanhamento e gerenciamento dos treinos dos alunos.")
    PROFESSOR,

    @Schema(description = "Aluno com acesso às funcionalidades destinadas aos clientes da academia.")
    ALUNO
}