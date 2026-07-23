package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.treinoexercicio.TreinoExercicioDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioResponseDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioUpdateDto;
import com.wictor.model.TreinoExercicioId;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.TreinoExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Treino Exercícios",
        description = """
                Endpoints responsáveis pelo gerenciamento dos exercícios
                vinculados aos treinos da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: gerenciamento.
                • PROFESSOR: gerenciamento dos treinos.
                • ALUNO: pode gerenciar e consultar os próprios exercícios.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/treinoexercicios")
@RequiredArgsConstructor
public class TreinoExercicioController {

    private final TreinoExercicioService treinoexercicioService;

    @Operation(
            summary = "Adicionar exercício ao treino",
            description = "Vincula um exercício existente a um treino."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Exercício adicionado ao treino com sucesso.",
            content = @Content(schema = @Schema(implementation = TreinoExercicioResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR','ALUNO')")
    @PostMapping
    public ResponseEntity<TreinoExercicioResponseDto> cadastrar(@RequestBody @Valid TreinoExercicioDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(treinoexercicioService.cadastrar(dto, user.getId()));
    }

    @Operation(
            summary = "Atualizar exercício do treino",
            description = "Atualiza as informações de um exercício vinculado ao treino."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = TreinoExercicioResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR','ALUNO')")
    @PatchMapping("/{treinoId}/{exercId}")
    public ResponseEntity<TreinoExercicioResponseDto> atualizar(
            @Parameter(description = "Identificador do treino.", example = "1")
            @PathVariable Integer treinoId,
            @Parameter(description = "Identificador do exercício.", example = "1")
            @PathVariable Integer exercId,
            @RequestBody @Valid TreinoExercicioUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);

        return ResponseEntity.ok(treinoexercicioService.atualizar(id, dto, user.getId()));
    }

    @Operation(
            summary = "Remover exercício do treino",
            description = "Remove o vínculo entre um exercício e um treino."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Exercício removido com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR','ALUNO')")
    @DeleteMapping("/{treinoId}/{exercId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador do treino.", example = "1")
            @PathVariable Integer treinoId,
            @Parameter(description = "Identificador do exercício.", example = "1")
            @PathVariable Integer exercId,
            @AuthenticationPrincipal CustomUserDetails user) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        treinoexercicioService.deletar(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar exercícios dos treinos",
            description = "Retorna todos os exercícios vinculados aos treinos."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = TreinoExercicioResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<TreinoExercicioResponseDto>> listar() {

        return ResponseEntity.ok(treinoexercicioService.listar());
    }

    @Operation(
            summary = "Buscar exercício do treino por ID",
            description = "Retorna um exercício específico vinculado a um treino."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Exercício encontrado com sucesso.",
            content = @Content(schema = @Schema(implementation = TreinoExercicioResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR','ALUNO')")
    @GetMapping("/{treinoId}/{exercId}")
    public ResponseEntity<TreinoExercicioResponseDto> buscarPorId(
            @Parameter(description = "Identificador do treino.", example = "1")
            @PathVariable Integer treinoId,
            @Parameter(description = "Identificador do exercício.", example = "1")
            @PathVariable Integer exercId,
            @AuthenticationPrincipal CustomUserDetails user) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        return ResponseEntity.ok(treinoexercicioService.buscarPorId(id, user.getId()));
    }
}