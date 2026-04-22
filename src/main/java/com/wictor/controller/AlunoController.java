package com.wictor.controller;

import com.wictor.Dto.AlunoDto;
import com.wictor.Dto.AlunoUpdateDto;
import com.wictor.Service.AlunoService;
import com.wictor.model.Aluno;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid AlunoDto dto) {

        Aluno aluno = alunoService.cadastrar(dto);
        return ResponseEntity.status(201).body(aluno);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id,
                                       @RequestBody @Valid AlunoUpdateDto dto) {

        return ResponseEntity.ok(alunoService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        alunoService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Aluno excluído"));
    }
}

