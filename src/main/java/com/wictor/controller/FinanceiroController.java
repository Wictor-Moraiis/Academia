package com.wictor.controller;

import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.service.FinanceiroService;
import com.wictor.model.Financeiro;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/financeiro")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this. financeiroService =  financeiroService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid FinanceiroDto dto) {

        Financeiro novo = financeiroService.cadastrar(dto);
        return ResponseEntity.status(201).body(novo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@PathVariable Integer id,
                                     @RequestBody @Valid FinanceiroUpdateDto dto) {

        Financeiro financeiro = financeiroService.atualizar(id, dto);
        return ResponseEntity.ok(financeiro);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        financeiroService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Plano excluído"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(financeiroService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Financeiro financeiro = financeiroService.buscarPorId(id);
        return ResponseEntity.ok(financeiro);
    }
}
