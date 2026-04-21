package com.wictor.controller;

import com.wictor.Dto.CategoriaDto;
import com.wictor.Service.CategoriaService;
import com.wictor.model.Categoria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid CategoriaDto dto) {

        Categoria novo = categoriaService.cadastrar(dto);
        return ResponseEntity.status(201).body(novo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@PathVariable Integer id,
                                     @RequestBody @Valid CategoriaDto dto) {

        Categoria categoria = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(categoria);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        categoriaService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Categoria excluída"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

}