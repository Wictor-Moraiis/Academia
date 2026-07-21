package com.wictor.controller;

import com.wictor.dto.produto.ProdutoDto;
import com.wictor.dto.produto.ProdutoResponseDto;
import com.wictor.dto.produto.ProdutoUpdateDto;
import com.wictor.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProdutoResponseDto> cadastrar(@RequestPart("dados") @Valid ProdutoDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(produtoService.cadastrar(dto, foto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Integer id,
                                                        @RequestPart("dados") @Valid ProdutoUpdateDto dto,
                                                        @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.ok(produtoService.atualizar(id, dto, foto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/desativar/{produtoId}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer produtoId) {

        produtoService.desativar(produtoId);
        return ResponseEntity.ok(Map.of("mensagem", "Produto desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{produtoId}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer produtoId) {

        produtoService.reativar(produtoId);
        return ResponseEntity.ok(Map.of("mensagem", "Produto reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer produtoId) {

        produtoService.deletar(produtoId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar() {

        return ResponseEntity.ok(produtoService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer produtoId) {

        return ResponseEntity.ok(produtoService.buscarPorId(produtoId));
    }

    @GetMapping("/reposicao")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    public ResponseEntity<List<ProdutoResponseDto>> buscarProdutosReposicao() {

        return ResponseEntity.ok(produtoService.buscarProdutosReposicao());
    }
}
