package com.wictor.controller;

import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.dto.venda.VendaDto;
import com.wictor.dto.venda.VendaResponseDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.FinanceiroRelatorioService;
import com.wictor.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/financeiros")
public class FinanceiroController {

    private final FinanceiroService financeiroService;
    private final FinanceiroRelatorioService financeiroRelatorioService;

    public FinanceiroController(FinanceiroService financeiroService,
    FinanceiroRelatorioService financeiroRelatorioService) {
        this.financeiroService = financeiroService;
        this.financeiroRelatorioService = financeiroRelatorioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<FinanceiroResponseDto> cadastrar(
            @RequestBody FinanceiroDto dto,
            @AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(financeiroService.cadastrar(dto, user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA')")
    @PostMapping("/venda")
    public ResponseEntity<VendaResponseDto> vender(
            @RequestBody @Valid VendaDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(financeiroService.vender(dto, user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA')")
    @PatchMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDto> atualizar(@PathVariable Integer id,
                                                         @RequestBody @Valid FinanceiroUpdateDto dto) {

        return ResponseEntity.ok(financeiroService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        financeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<FinanceiroResponseDto>> listar(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(financeiroService.listar(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDto> buscarPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(financeiroService.buscarPorId(id, user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorio(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {

        byte[] pdf = financeiroRelatorioService.gerarRelatorio(inicio, fim);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-financeiro.pdf").contentType(MediaType.APPLICATION_PDF).body(pdf);
    }
}
