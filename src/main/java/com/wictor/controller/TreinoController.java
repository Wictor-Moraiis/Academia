package com.wictor.controller;

import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treinoexercicio.TreinoResponseDto;
import com.wictor.dto.treinoexercicio.TreinoUpdateDto;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<TreinoResponseDto> cadastrar(
            @RequestBody @Valid TreinoDto dto,
            @AuthenticationPrincipal User logado) {

        return ResponseEntity.status(201).body(treinoService.cadastrar(dto, logado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<TreinoResponseDto> alterar(@PathVariable Integer id,
                                     @RequestBody @Valid TreinoUpdateDto dto,
                                     @AuthenticationPrincipal User logado) {

        return ResponseEntity.ok(treinoService.atualizar(id, dto, logado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer id,
                                       @AuthenticationPrincipal User logado){

        treinoService.desativar(id, logado);
        return ResponseEntity.ok(Map.of("mensagem", "Treino desativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping
    public ResponseEntity<List<TreinoResponseDto>> listar() {
        return ResponseEntity.ok(treinoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(treinoService.buscarPorId(id));
    }
}
