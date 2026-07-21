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
    public ResponseEntity<PlanoResponseDto> cadastrar(@RequestBody @Valid PlanoDto dto) {

        return ResponseEntity.status(201).body(planoService.cadastrar(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/{planoId}")
    public ResponseEntity<PlanoResponseDto> atualizar(@PathVariable Integer planoId,
            @RequestBody @Valid PlanoUpdateDto dto) {

        return ResponseEntity.ok(planoService.atualizar(planoId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/desativar/{planoId}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer planoId) {

        planoService.desativar(planoId);
        return ResponseEntity.ok(Map.of("mensagem", "Plano desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/reativar/{planoId}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer planoId) {

        planoService.reativar(planoId);
        return ResponseEntity.ok(Map.of("mensagem", "Plano reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{planoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer planoId) {

        planoService.deletar(planoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseDto>> listar(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(planoService.listar(user));
    }

    @GetMapping("/{planoId}")
    public ResponseEntity<PlanoResponseDto> buscarPorId(@PathVariable Integer planoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(planoService.buscarPorId(planoId, user));
    }

}