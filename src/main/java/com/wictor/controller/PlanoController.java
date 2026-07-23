package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.plano.PlanoDto;
import com.wictor.dto.plano.PlanoResponseDto;
import com.wictor.dto.plano.PlanoUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.PlanoService;
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
        name = "Planos",
        description = """
                Endpoints responsáveis pelo gerenciamento dos planos da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, consulta e atualização.
                • RECEPCIONISTA: consulta.
                • PROFESSOR: consulta.
                • ALUNO: consulta planos disponíveis.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @Operation(
            summary = "Cadastrar plano",
            description = "Cria um novo plano de academia."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Plano cadastrado com sucesso.",
            content = @Content(schema = @Schema(implementation = PlanoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping
    public ResponseEntity<PlanoResponseDto> cadastrar(@RequestBody @Valid PlanoDto dto) {

        return ResponseEntity.status(201).body(planoService.cadastrar(dto));
    }

    @Operation(
            summary = "Atualizar plano",
            description = "Atualiza os dados de um plano existente."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Plano atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = PlanoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/{planoId}")
    public ResponseEntity<PlanoResponseDto> atualizar(
            @Parameter(description = "Identificador único do plano.", example = "1")
            @PathVariable Integer planoId,
            @RequestBody @Valid PlanoUpdateDto dto) {

        return ResponseEntity.ok(planoService.atualizar(planoId, dto));
    }

    @Operation(
            summary = "Desativar plano",
            description = "Remove um plano da disponibilidade para novos alunos."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Plano desativado com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/desativar/{planoId}")
    public ResponseEntity<Map<String, String>> desativar(
            @Parameter(description = "Identificador único do plano.", example = "1")
            @PathVariable Integer planoId) {

        planoService.desativar(planoId);
        return ResponseEntity.ok(Map.of("mensagem", "Plano desativado"));
    }

    @Operation(
            summary = "Reativar plano",
            description = "Disponibiliza novamente um plano desativado."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Plano reativado com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{planoId}")
    public ResponseEntity<Map<String, String>> reativar(
            @Parameter(description = "Identificador único do plano.", example = "1")
            @PathVariable Integer planoId) {

        planoService.reativar(planoId);
        return ResponseEntity.ok(Map.of("mensagem", "Plano reativado"));
    }

    @Operation(
            summary = "Excluir plano",
            description = "Remove definitivamente um plano do sistema."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Plano excluído com sucesso.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{planoId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único do plano.", example = "1")
            @PathVariable Integer planoId) {

        planoService.deletar(planoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar planos",
            description = "Retorna os planos disponíveis para o usuário."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Planos retornados com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = PlanoResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping
    public ResponseEntity<List<PlanoResponseDto>> listar(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(planoService.listar(user));
    }

    @Operation(
            summary = "Buscar plano por ID",
            description = "Retorna os dados de um plano específico."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Plano encontrado.",
            content = @Content(schema = @Schema(implementation = PlanoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping("/{planoId}")
    public ResponseEntity<PlanoResponseDto> buscarPorId(
            @Parameter(description = "Identificador único do plano.", example = "1")
            @PathVariable Integer planoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(planoService.buscarPorId(planoId, user));
    }
}