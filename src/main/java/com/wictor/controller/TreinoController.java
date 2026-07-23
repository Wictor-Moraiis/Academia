package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treino.TreinoResponseDto;
import com.wictor.dto.treino.TreinoUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.TreinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Treinos",
        description = """
                Endpoints responsáveis pelo gerenciamento dos treinos
                da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: gerenciamento de treinos.
                • PROFESSOR: criação, atualização e consulta.
                • ALUNO: consulta dos próprios treinos.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/treinos")
public class TreinoController {

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }

    @Operation(
            summary = "Cadastrar treino",
            description = "Cria um novo treino para um aluno."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Treino cadastrado com sucesso.",
            content = @Content(schema = @Schema(implementation = TreinoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PostMapping
    public ResponseEntity<TreinoResponseDto> cadastrar(@RequestBody @Valid TreinoDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(treinoService.cadastrar(dto, user.getId()));
    }

    @Operation(
            summary = "Atualizar treino",
            description = "Atualiza os dados de um treino existente."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Treino atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = TreinoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PatchMapping("/{treinoId}")
    public ResponseEntity<TreinoResponseDto> atualizar(
            @Parameter(description = "Identificador único do treino.", example = "1")
            @PathVariable Integer treinoId,
            @RequestBody @Valid TreinoUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(treinoService.atualizar(treinoId, dto, user.getId()));
    }

    @Operation(
            summary = "Desativar treino",
            description = "Desativa um treino ativo."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Treino desativado com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PatchMapping("/desativar/{treinoId}")
    public ResponseEntity<Map<String, String>> desativar(
            @Parameter(description = "Identificador único do treino.", example = "1")
            @PathVariable Integer treinoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        treinoService.desativar(treinoId, user.getId());
        return ResponseEntity.ok(Map.of("mensagem", "Treino desativado"));
    }

    @Operation(
            summary = "Reativar treino",
            description = "Reativa um treino desativado."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Treino reativado com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{treinoId}")
    public ResponseEntity<Map<String, String>> reativar(
            @Parameter(description = "Identificador único do treino.", example = "1")
            @PathVariable Integer treinoId) {

        treinoService.reativar(treinoId);
        return ResponseEntity.ok(Map.of("mensagem", "Treino reativado"));
    }

    @Operation(
            summary = "Excluir treino",
            description = "Remove definitivamente um treino."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Treino excluído com sucesso.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{treinoId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único do treino.", example = "1")
            @PathVariable Integer treinoId) {

        treinoService.deletar(treinoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar treinos",
            description = "Retorna todos os treinos cadastrados."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Treinos retornados com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = TreinoResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<TreinoResponseDto>> listar() {

        return ResponseEntity.ok(treinoService.listar());
    }

    @Operation(
            summary = "Buscar treino por ID",
            description = """
                    Retorna um treino específico.

                    Alunos podem consultar apenas os próprios treinos.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Treino encontrado.",
            content = @Content(schema = @Schema(implementation = TreinoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR','ALUNO')")
    @GetMapping("/{treinoId}")
    public ResponseEntity<TreinoResponseDto> buscarPorId(
            @Parameter(description = "Identificador único do treino.", example = "1")
            @PathVariable Integer treinoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(treinoService.buscarPorId(treinoId, user.getId()));
    }
}
