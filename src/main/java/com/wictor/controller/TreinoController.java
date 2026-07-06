package com.wictor.controller;

import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treino.TreinoResponseDto;
import com.wictor.dto.treino.TreinoUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.TreinoService;
import com.wictor.model.User;
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
    public ResponseEntity<TreinoResponseDto> cadastrar(
            @RequestBody @Valid TreinoDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201)
                .body(treinoService.cadastrar(dto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<TreinoResponseDto> atualizar(@PathVariable Integer id,
                                     @RequestBody @Valid TreinoUpdateDto dto,
                                                     @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(treinoService.atualizar(id, dto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer id,
                                                         @AuthenticationPrincipal CustomUserDetails user){

        treinoService.desativar(id, user.getId());
        return ResponseEntity.ok(Map.of("mensagem", "Treino desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer id) {
        treinoService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Treino reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        treinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<TreinoResponseDto>> listar() {
        return ResponseEntity.ok(treinoService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'PROFESSOR', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponseDto> buscarPorId(@PathVariable Integer id,
                                                         @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(treinoService.buscarPorId(id, user.getId()));
    }
}
