package com.wictor.controller;

import com.wictor.Dto.*;
import com.wictor.Service.PlanoService;
import com.wictor.model.Plano;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/planos")

public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this. planoService =  planoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid PlanoDto dto) {

        Plano novo = planoService.cadastrar(dto);
        return ResponseEntity.status(201).body(novo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@PathVariable Integer id,
            @RequestBody @Valid PlanoDto dto) {

        Plano plano = planoService.atualizar(id, dto);
        return ResponseEntity.ok(plano);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        planoService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Plano excluído"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(planoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Plano plano = planoService.buscarPorId(id);
        return ResponseEntity.ok(plano);
    }

}