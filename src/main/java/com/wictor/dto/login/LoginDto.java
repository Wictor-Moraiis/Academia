package com.wictor.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais utilizadas para autenticação")
public record LoginDto(

        @Schema(description = "CPF do usuário", example = "12345678900")
        String cpf,

        @Schema(description = "Senha da conta", example = "MinhaSenha@123")
        String senha
){}