package com.wictor.controller;

import com.wictor.dto.pagamento.AlterarPlanoResponseDto;
import com.wictor.dto.pagamento.CheckoutPagamentoResponseDto;
import com.wictor.dto.pagamento.PagamentoPresencialDto;
import com.wictor.dto.pagamento.PagamentoResponseDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @PostMapping("/presencial")
    public ResponseEntity<PagamentoResponseDto> pagarPresencial(@RequestBody @Valid PagamentoPresencialDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(pagamentoService.pagamentoPresencial(dto,user));
    }

    @PostMapping("/checkout/{planoId}")
    public ResponseEntity<CheckoutPagamentoResponseDto> checkout(@PathVariable Integer planoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.checkout(planoId, user));
    }

    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/assinatura/cancelar")
    public ResponseEntity<Void> cancelarAssinatura(@AuthenticationPrincipal CustomUserDetails user) {

        pagamentoService.cancelarAssinatura(user);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/assinatura/alterar/{planoId}")
    public ResponseEntity<AlterarPlanoResponseDto> alterarPlano(@PathVariable Integer planoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.alterarPlano(planoId, user));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','ALUNO')")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDto>> listar(@AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.listar(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','ALUNO')")
    @GetMapping("/{pagamentoId}")
    public ResponseEntity<PagamentoResponseDto> buscarPorId(@PathVariable Integer pagamentoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.buscarPorId(pagamentoId, user));
    }
}