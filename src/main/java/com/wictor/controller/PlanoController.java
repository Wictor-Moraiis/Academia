package com.wictor.controller;

import com.wictor.dto.plano.PlanoDto;
import com.wictor.dto.plano.PlanoResponseDto;
import com.wictor.dto.plano.PlanoUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.PlanoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/planos")

public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<PlanoResponseDto> cadastrar(
            @RequestBody @Valid PlanoDto dto) {

        return ResponseEntity.status(201).body(planoService.cadastrar(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/{id}")
    public ResponseEntity<PlanoResponseDto> atualizar(@PathVariable Integer id,
            @RequestBody @Valid PlanoUpdateDto dto) {

        return ResponseEntity.ok(planoService.atualizar(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer id) {

        planoService.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Plano desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer id) {
        planoService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Plano reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        planoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseDto>> listar(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(planoService.listar(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDto> buscarPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(planoService.buscarPorId(id, user));
    }

}