package com.wictor.controller;

import com.wictor.dto.treino.TreinoExercicioDto;
import com.wictor.dto.treino.TreinoExercicioResponseDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioUpdateDto;
import com.wictor.service.TreinoExercicioService;
import com.wictor.model.TreinoExercicio;
import com.wictor.model.TreinoExercicioId;
import com.wictor.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/treinoexercicios")
public class TreinoExercicioController {

    private final TreinoExercicioService treinoexercicioService;

    public TreinoExercicioController(TreinoExercicioService treinoexercicioService) {
        this.treinoexercicioService =  treinoexercicioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid TreinoExercicioDto dto,
            @AuthenticationPrincipal User logado) {

        TreinoExercicio novo = treinoexercicioService.cadastrar(dto, logado);

        return ResponseEntity.status(201)
                .body(new TreinoExercicioResponseDto(
                        novo.getTreino().getId(),
                        novo.getTreino().getNome(),
                        novo.getExercicio().getId(),
                        novo.getExercicio().getNome(),
                        novo.getOrdem(),
                        novo.getCarga(),
                        novo.getSeries(),
                        novo.getRep(),
                        novo.getObs()
                ));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/alterar/{treinoId}/{exercId}")
    public ResponseEntity<?> alterar(
            @PathVariable Integer treinoId,
            @PathVariable Integer exercId,
            @RequestBody @Valid TreinoExercicioUpdateDto dto,
            @AuthenticationPrincipal User logado) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);

        TreinoExercicio te = treinoexercicioService.atualizar(id, dto, logado);

        return ResponseEntity.ok(
                new TreinoExercicioResponseDto(
                        te.getTreino().getId(),
                        te.getTreino().getNome(),
                        te.getExercicio().getId(),
                        te.getExercicio().getNome(),
                        te.getOrdem(),
                        te.getCarga(),
                        te.getSeries(),
                        te.getRep(),
                        te.getObs()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/excluir/{treinoId}/{exercId}")
    public ResponseEntity<?> deletar(
            @PathVariable Integer treinoId,
            @PathVariable Integer exercId,
            @AuthenticationPrincipal User logado) {

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);

        treinoexercicioService.deletar(id, logado);

        return ResponseEntity.ok(
                Map.of("mensagem", "Exercício do treino excluído"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(treinoexercicioService.listar());
    }

    @GetMapping("/{treinoId}/{exercId}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer treinoId,
                                         @PathVariable Integer exercId){

        TreinoExercicioId id = new TreinoExercicioId(treinoId, exercId);
        TreinoExercicio te = treinoexercicioService.buscarPorId(id);

        return ResponseEntity.ok(
                new TreinoExercicioResponseDto(
                        te.getTreino().getId(),
                        te.getTreino().getNome(),
                        te.getExercicio().getId(),
                        te.getExercicio().getNome(),
                        te.getOrdem(),
                        te.getCarga(),
                        te.getSeries(),
                        te.getRep(),
                        te.getObs()
                )
        );
    }
}