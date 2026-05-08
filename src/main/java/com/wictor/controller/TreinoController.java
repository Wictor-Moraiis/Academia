package com.wictor.controller;

import com.wictor.Dto.TreinoDto;
import com.wictor.Dto.TreinoUpdateDto;
import com.wictor.Service.TreinoService;
import com.wictor.model.Treino;
import com.wictor.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/treinos")
public class TreinoController {

    private final TreinoService TreinoService;

    public TreinoController(TreinoService TreinoService) {
        this.TreinoService =  TreinoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUNO')")
    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid TreinoDto dto,
            @AuthenticationPrincipal User logado) {

        Treino novo = TreinoService.cadastrar(dto, logado);
        return ResponseEntity.status(201).body(novo);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUNO')")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@PathVariable Integer id,
                                     @RequestBody @Valid TreinoUpdateDto dto,
                                     @AuthenticationPrincipal User logado) {

        Treino treino = TreinoService.atualizar(id, dto, logado);
        return ResponseEntity.ok(treino);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUNO')")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<?> desativar(@PathVariable Integer id,
                                       @AuthenticationPrincipal User logado){

        TreinoService.desativar(id, logado);
        return ResponseEntity.ok(Map.of("mensagem", "Treino desativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("reativar/{id}")
    public ResponseEntity<?> reativar(@PathVariable Integer id) {
        TreinoService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Treino reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        TreinoService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Treino excluído"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(TreinoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Treino treino = TreinoService.buscarPorId(id);
        return ResponseEntity.ok(treino);
    }
}
