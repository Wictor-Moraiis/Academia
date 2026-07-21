package com.wictor.controller;

import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treino.TreinoResponseDto;
import com.wictor.dto.treino.TreinoUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.TreinoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/treinos")
public class TreinoController {

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @PostMapping
    public ResponseEntity<TreinoResponseDto> cadastrar(@RequestBody @Valid TreinoDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(treinoService.cadastrar(dto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @PatchMapping("/{treinoId}")
    public ResponseEntity<TreinoResponseDto> atualizar(@PathVariable Integer treinoId,
                                     @RequestBody @Valid TreinoUpdateDto dto,
                                                       @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(treinoService.atualizar(treinoId, dto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @PatchMapping("/desativar/{treinoId}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer treinoId,
                                                         @AuthenticationPrincipal CustomUserDetails user) {

        treinoService.desativar(treinoId, user.getId());
        return ResponseEntity.ok(Map.of("mensagem", "Treino desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/reativar/{treinoId}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer treinoId) {

        treinoService.reativar(treinoId);
        return ResponseEntity.ok(Map.of("mensagem", "Treino reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{treinoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer treinoId) {

        treinoService.deletar(treinoId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<TreinoResponseDto>> listar() {

        return ResponseEntity.ok(treinoService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'PROFESSOR', 'ALUNO')")
    @GetMapping("/{treinoId}")
    public ResponseEntity<TreinoResponseDto> buscarPorId(@PathVariable Integer treinoId,
                                                         @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(treinoService.buscarPorId(treinoId, user.getId()));
    }
}
