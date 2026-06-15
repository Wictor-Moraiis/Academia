package com.wictor.controller;

import com.wictor.dto.plano.PlanoDto;
import com.wictor.dto.plano.PlanoResponseDto;
import com.wictor.dto.plano.PlanoUpdateDto;
import com.wictor.service.PlanoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")

public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PlanoResponseDto> cadastrar(
            @RequestBody @Valid PlanoDto dto) {

        return ResponseEntity.status(201).body(planoService.cadastrar(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<PlanoResponseDto> alterar(@PathVariable Integer id,
            @RequestBody @Valid PlanoUpdateDto dto) {

        return ResponseEntity.ok(planoService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        planoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseDto>> listar() {
        return ResponseEntity.ok(planoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(planoService.buscarPorId(id));
    }

}