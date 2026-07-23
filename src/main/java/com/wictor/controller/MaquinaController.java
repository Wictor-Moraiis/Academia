package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.maquina.MaquinaDto;
import com.wictor.dto.maquina.MaquinaResponseDto;
import com.wictor.dto.maquina.MaquinaUpdateDto;
import com.wictor.service.MaquinaService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Máquinas",
        description = """
                Endpoints responsáveis pelo gerenciamento das máquinas
                disponíveis na academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, atualização e gerenciamento.
                • PROFESSOR: desativação e consulta.
                • RECEPCIONISTA: consulta.
                • ALUNO: consulta.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @Operation(
            summary = "Cadastrar máquina",
            description = """
                    Realiza o cadastro de uma nova máquina
                    no sistema da academia.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Máquina cadastrada com sucesso.",
            content = @Content(schema = @Schema(implementation = MaquinaResponseDto.class)))
    @ApiResponse(responseCode = "409", description = "Máquina já cadastrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping
    public ResponseEntity<MaquinaResponseDto> cadastrar(
            @RequestBody @Valid MaquinaDto dto) {

        return ResponseEntity.status(201).body(maquinaService.cadastrar(dto));
    }

    @Operation(
            summary = "Atualizar máquina",
            description = """
                    Atualiza os dados de uma máquina existente.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Máquina atualizada com sucesso.",
            content = @Content(schema = @Schema(implementation = MaquinaResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Máquina não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/{maquinaId}")
    public ResponseEntity<MaquinaResponseDto> atualizar(
            @Parameter(description = "Identificador único da máquina.", example = "1")
            @PathVariable Integer maquinaId,
            @RequestBody @Valid MaquinaUpdateDto dto) {

        return ResponseEntity.ok(maquinaService.atualizar(maquinaId, dto));
    }

    @Operation(
            summary = "Desativar máquina",
            description = """
                    Desativa uma máquina.

                    A máquina permanece cadastrada,
                    porém deixa de estar disponível para uso.
                    
                    Permitido para ADMIN, GERENTE e PROFESSOR.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Máquina desativada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Máquina não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PatchMapping("/desativar/{maquinaId}")
    public ResponseEntity<Map<String, String>> desativar(
            @Parameter(description = "Identificador único da máquina.", example = "1")
            @PathVariable Integer maquinaId) {

        maquinaService.desativar(maquinaId);
        return ResponseEntity.ok(Map.of("mensagem", "Máquina desativada"));
    }


    @Operation(
            summary = "Reativar máquina",
            description = """
                    Reativa uma máquina previamente desativada,
                    tornando-a disponível novamente.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Máquina reativada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Máquina não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{maquinaId}")
    public ResponseEntity<Map<String, String>> reativar(
            @Parameter(description = "Identificador único da máquina.", example = "1")
            @PathVariable Integer maquinaId) {

        maquinaService.reativar(maquinaId);
        return ResponseEntity.ok(Map.of("mensagem",
                "Máquina reativada"));
    }

    @Operation(
            summary = "Excluir máquina",
            description = """
                    Remove uma máquina do sistema.

                    Operação exclusiva para administradores.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Máquina excluída com sucesso.")
    @ApiResponse(responseCode = "404", description = "Máquina não encontrada.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{maquinaId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único da máquina.", example = "1")
            @PathVariable Integer maquinaId) {

        maquinaService.deletar(maquinaId);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Listar máquinas",
            description = """
                    Retorna todas as máquinas cadastradas.

                    Disponível para todos os perfis autenticados.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Lista de máquinas retornada com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = MaquinaResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping
    public ResponseEntity<List<MaquinaResponseDto>> listar() {

        return ResponseEntity.ok(maquinaService.listar());
    }

    @Operation(
            summary = "Buscar máquina por ID",
            description = """
                    Retorna os dados completos de uma máquina.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Máquina encontrada com sucesso.",
            content = @Content(schema = @Schema(implementation = MaquinaResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Máquina não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping("/{maquinaId}")
    public ResponseEntity<MaquinaResponseDto> buscarPorId(
            @Parameter(description = "Identificador único da máquina.", example = "1")
            @PathVariable Integer maquinaId) {

        return ResponseEntity.ok(maquinaService.buscarPorId(maquinaId));
    }
}