package com.wictor.config.swagger.annotation;

import com.wictor.dto.erro.ErrorResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({

        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada.",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        ),

        @ApiResponse(responseCode = "409", description = "Conflito de dados.",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        ),

        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        )
})
public @interface ApiPublicResponses {
}
