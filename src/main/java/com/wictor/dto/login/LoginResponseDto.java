package com.wictor.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta retornada após autenticação bem-sucedida")
public record LoginResponseDto(

        @Schema(description = "Token JWT utilizado para autenticar as requisições",
                example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Identificador do usuário autenticado", example = "1")
        Integer id
){}