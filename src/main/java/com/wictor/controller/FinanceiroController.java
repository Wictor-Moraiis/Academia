package com.wictor.controller;

import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/financeiros")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FinanceiroResponseDto> cadastrar(
            @RequestBody @Valid FinanceiroDto dto) {

        return ResponseEntity.status(201).body(financeiroService.cadastrar(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDto> alterar(@PathVariable Integer id,
                                     @RequestBody @Valid FinanceiroUpdateDto dto) {

        return ResponseEntity.ok(financeiroService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        financeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FinanceiroResponseDto>> listar() {
        return ResponseEntity.ok(financeiroService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(financeiroService.buscarPorId(id));
    }
}
