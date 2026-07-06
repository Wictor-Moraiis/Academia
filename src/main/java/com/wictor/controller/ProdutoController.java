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
    public ResponseEntity<ProdutoResponseDto> cadastrar(
            @RequestPart("dados") @Valid ProdutoDto dto,
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
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer id) {

        produtoService.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Produto desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer id) {
        produtoService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Produto reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }
}
