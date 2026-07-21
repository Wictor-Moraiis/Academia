package com.wictor.controller;

import com.wictor.dto.maquina.MaquinaDto;
import com.wictor.dto.maquina.MaquinaResponseDto;
import com.wictor.dto.maquina.MaquinaUpdateDto;
import com.wictor.service.MaquinaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<MaquinaResponseDto> cadastrar(@RequestBody @Valid MaquinaDto dto) {

        return ResponseEntity.status(201).body(maquinaService.cadastrar(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/{maquinaId}")
    public ResponseEntity<MaquinaResponseDto> atualizar(@PathVariable Integer maquinaId,
                                     @RequestBody @Valid MaquinaUpdateDto dto) {

        return ResponseEntity.ok(maquinaService.atualizar(maquinaId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'PROFESSOR')")
    @PatchMapping("/desativar/{maquinaId}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer maquinaId) {

        maquinaService.desativar(maquinaId);
        return ResponseEntity.ok(Map.of("mensagem", "Máquina desativada"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/reativar/{maquinaId}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer maquinaId) {

        maquinaService.reativar(maquinaId);
        return ResponseEntity.ok(Map.of("mensagem", "Máquina reativada"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{maquinaId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer maquinaId) {

        maquinaService.deletar(maquinaId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA', 'PROFESSOR', 'ALUNO')")
    @GetMapping
    public ResponseEntity<List<MaquinaResponseDto>> listar() {

        return ResponseEntity.ok(maquinaService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'RECEPCIONISTA', 'PROFESSOR', 'ALUNO')")
    @GetMapping("/{maquinaId}")
    public ResponseEntity<MaquinaResponseDto> buscarPorId(@PathVariable Integer maquinaId) {

        return ResponseEntity.ok(maquinaService.buscarPorId(maquinaId));
    }
}
