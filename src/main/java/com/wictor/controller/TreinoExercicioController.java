package com.wictor.controller;

import com.wictor.dto.treinoexercicio.TreinoExercicioDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioResponseDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.TreinoExercicioService;
import com.wictor.model.TreinoExercicioId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treinoexercicios")
public class TreinoExercicioController {

    private final TreinoExercicioService treinoexercicioService;

    public TreinoExercicioController(TreinoExercicioService treinoexercicioService) {
        this.treinoexercicioService = treinoexercicioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR', 'ALUNO')")
    @PostMapping
    public ResponseEntity<TreinoExercicioResponseDto> cadastrar(@RequestBody @Valid TreinoExercicioDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(treinoexercicioService.cadastrar(dto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR', 'ALUNO')")
    @PatchMapping("/{treinoId}/{exercId}")
    public ResponseEntity<TreinoExercicioResponseDto> atualizar(@PathVariable Integer treinoId,
            @PathVariable Integer exercId,
            @RequestBody @Valid TreinoExercicioUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        return ResponseEntity.ok(treinoexercicioService.atualizar(id, dto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR', 'ALUNO')")
    @DeleteMapping("/{treinoId}/{exercId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer treinoId, @PathVariable Integer exercId,
            @AuthenticationPrincipal CustomUserDetails user) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        treinoexercicioService.deletar(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<TreinoExercicioResponseDto>> listar() {

        return ResponseEntity.ok(treinoexercicioService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR', 'ALUNO')")
    @GetMapping("/{treinoId}/{exercId}")
    public ResponseEntity<TreinoExercicioResponseDto> buscarPorId(@PathVariable Integer treinoId,
                                         @PathVariable Integer exercId,
                                                                  @AuthenticationPrincipal CustomUserDetails user) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        return ResponseEntity.ok(treinoexercicioService.buscarPorId(id, user.getId()));
    }
}