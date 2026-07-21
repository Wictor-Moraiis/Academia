package com.wictor.controller;

import com.wictor.dto.categoria.CategoriaDto;
import com.wictor.dto.categoria.CategoriaResponseDto;
import com.wictor.dto.categoria.CategoriaUpdateDto;
import com.wictor.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> cadastrar(@RequestBody @Valid CategoriaDto dto) {

        return ResponseEntity.status(201).body(categoriaService.cadastrar(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/{categoriaId}")
    public ResponseEntity<CategoriaResponseDto> atualizar(@PathVariable Integer categoriaId,
                                     @RequestBody @Valid CategoriaUpdateDto dto) {

        return ResponseEntity.ok(categoriaService.atualizar(categoriaId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer categoriaId) {

        categoriaService.deletar(categoriaId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> listar() {

        return ResponseEntity.ok(categoriaService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{categoriaId}")
    public ResponseEntity<CategoriaResponseDto> buscarPorId(@PathVariable Integer categoriaId) {

        return ResponseEntity.ok(categoriaService.buscarPorId(categoriaId));
    }

}