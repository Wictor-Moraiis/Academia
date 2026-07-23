package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.produto.ProdutoDto;
import com.wictor.dto.produto.ProdutoResponseDto;
import com.wictor.dto.produto.ProdutoUpdateDto;
import com.wictor.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Produtos",
        description = """
                Endpoints responsáveis pelo gerenciamento dos produtos
                e controle de estoque da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, consulta e atualização.
                • RECEPCIONISTA: consulta e venda.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(
            summary = "Cadastrar produto",
            description = """
                    Cadastra um novo produto no estoque.

                    A requisição deve ser enviada como multipart/form-data:
                    • dados: informações do produto.
                    • foto: imagem do produto (opcional).
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso.",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProdutoResponseDto> cadastrar(
            @RequestPart("dados") @Valid ProdutoDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(produtoService.cadastrar(dto, foto));
    }

    @Operation(
            summary = "Atualizar produto",
            description = """
                    Atualiza os dados de um produto existente.

                    Permite alteração das informações e atualização da imagem.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping(value = "/{produtoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponseDto> atualizar(
            @Parameter(description = "Identificador único do produto.", example = "1")
            @PathVariable Integer produtoId,
            @RequestPart("dados") @Valid ProdutoUpdateDto dto,
            @RequestPart(value = "foto", required = false)
            MultipartFile foto) {

        return ResponseEntity.ok(produtoService.atualizar(produtoId, dto, foto));
    }

    @Operation(
            summary = "Desativar produto",
            description = "Remove o produto da disponibilidade de venda."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Produto desativado com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/desativar/{produtoId}")
    public ResponseEntity<Map<String, String>> desativar(
            @Parameter(description = "Identificador único do produto.", example = "1")
            @PathVariable Integer produtoId) {

        produtoService.desativar(produtoId);
        return ResponseEntity.ok(Map.of("mensagem", "Produto desativado"));
    }

    @Operation(
            summary = "Reativar produto",
            description = "Retorna um produto desativado para disponibilidade."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Produto reativado com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{produtoId}")
    public ResponseEntity<Map<String, String>> reativar(
            @Parameter(description = "Identificador único do produto.", example = "1")
            @PathVariable Integer produtoId) {

        produtoService.reativar(produtoId);

        return ResponseEntity.ok(Map.of("mensagem", "Produto reativado"));
    }

    @Operation(
            summary = "Excluir produto",
            description = "Remove definitivamente um produto do sistema."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único do produto.", example = "1")
            @PathVariable Integer produtoId) {

        produtoService.deletar(produtoId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar produtos",
            description = "Retorna todos os produtos disponíveis."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = ProdutoResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar() {

        return ResponseEntity.ok(produtoService.listar());
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Produto encontrado.",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(
            @Parameter(description = "Identificador único do produto.", example = "1")
            @PathVariable Integer produtoId) {

        return ResponseEntity.ok(produtoService.buscarPorId(produtoId));
    }

    @Operation(
            summary = "Buscar produtos para reposição",
            description = """
                    Retorna produtos que atingiram o estoque mínimo
                    configurado e precisam de reposição.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Produtos para reposição retornados com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = ProdutoResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/reposicao")
    public ResponseEntity<List<ProdutoResponseDto>> buscarProdutosReposicao() {

        return ResponseEntity.ok(produtoService.buscarProdutosReposicao());
    }
}
