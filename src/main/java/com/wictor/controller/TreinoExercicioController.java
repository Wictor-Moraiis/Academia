package com.wictor.controller;

import com.wictor.dto.treinoexercicio.TreinoExercicioDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioResponseDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioUpdateDto;
import com.wictor.service.TreinoExercicioService;
import com.wictor.model.TreinoExercicioId;
import com.wictor.model.User;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<TreinoExercicioResponseDto> cadastrar(
            @RequestBody @Valid TreinoExercicioDto dto,
            @AuthenticationPrincipal User logado) {

        return ResponseEntity.status(201).body(treinoexercicioService.cadastrar(dto, logado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{treinoId}/{exercId}")
    public ResponseEntity<TreinoExercicioResponseDto> alterar(
            @PathVariable Integer treinoId,
            @PathVariable Integer exercId,
            @RequestBody @Valid TreinoExercicioUpdateDto dto,
            @AuthenticationPrincipal User logado) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);

        return ResponseEntity.ok(treinoexercicioService.atualizar(id, dto, logado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{treinoId}/{exercId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer treinoId,
            @PathVariable Integer exercId,
            @AuthenticationPrincipal User logado) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);

        treinoexercicioService.deletar(id, logado);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TreinoExercicioResponseDto>> listar() {
        return ResponseEntity.ok(treinoexercicioService.listar());
    }

    @GetMapping("/{treinoId}/{exercId}")
    public ResponseEntity<TreinoExercicioResponseDto> buscarPorId(@PathVariable Integer treinoId,
                                         @PathVariable Integer exercId){

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        return ResponseEntity.ok(treinoexercicioService.buscarPorId(id));
    }
}