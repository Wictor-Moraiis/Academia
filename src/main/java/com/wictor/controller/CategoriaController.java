package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.categoria.CategoriaDto;
import com.wictor.dto.categoria.CategoriaResponseDto;
import com.wictor.dto.categoria.CategoriaUpdateDto;
import com.wictor.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(
        name = "Categorias",
        description = """
                Endpoints responsáveis pelo gerenciamento das categorias
                de exercícios da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, consulta e atualização.
                • RECEPCIONISTA: somente consulta.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(
            summary = "Cadastrar categoria",
            description = """
                    Realiza o cadastro de uma nova categoria de exercício.

                    Exemplos:
                    • Peito
                    • Costas
                    • Pernas
                    • Ombro
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso.",
            content = @Content(schema = @Schema(implementation = CategoriaResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> cadastrar(
            @RequestBody @Valid CategoriaDto dto) {

        return ResponseEntity.status(201).body(categoriaService.cadastrar(dto));
    }

    @Operation(
            summary = "Atualizar categoria",
            description = """
                    Atualiza os dados de uma categoria existente.

                    Permitido para administradores e gerentes.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso.",
            content = @Content(schema = @Schema(implementation = CategoriaResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/{categoriaId}")
    public ResponseEntity<CategoriaResponseDto> atualizar(
            @Parameter(description = "Identificador único da categoria.", example = "1")
            @PathVariable Integer categoriaId,
            @RequestBody @Valid CategoriaUpdateDto dto) {

        return ResponseEntity.ok(categoriaService.atualizar(categoriaId, dto));
    }

    @Operation(
            summary = "Excluir categoria",
            description = """
                    Remove uma categoria do sistema.

                    Operação exclusiva para administradores.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único da categoria.", example = "1")
            @PathVariable Integer categoriaId) {

        categoriaService.deletar(categoriaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar categorias",
            description = """
                    Retorna todas as categorias cadastradas.

                    Utilizado para associação de exercícios.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Categorias retornadas com sucesso.",
            content = @Content(mediaType = "application/json", schema = @Schema(
                            implementation = CategoriaResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> listar() {

        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(
            summary = "Buscar categoria por ID",
            description = """
                    Retorna uma categoria específica através do identificador.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso.",
            content = @Content(schema = @Schema(implementation = CategoriaResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{categoriaId}")
    public ResponseEntity<CategoriaResponseDto> buscarPorId(
            @Parameter(description = "Identificador único da categoria.", example = "1")
            @PathVariable Integer categoriaId) {

        return ResponseEntity.ok(categoriaService.buscarPorId(categoriaId));
    }
}