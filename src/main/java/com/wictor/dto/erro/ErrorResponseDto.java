package com.wictor.dto.erro;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padrão para erros da API.")
public record ErrorResponseDto(

        @Schema(description = "Mensagem descritiva do erro.", example = "Aluno não encontrado.")
        String erro
){}