package com.wictor.controller;

import com.wictor.dto.aluno.AlunoDto;
import com.wictor.dto.aluno.AlunoResponseDto;
import com.wictor.dto.aluno.AlunoUpdateDto;
import com.wictor.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alunos")
public class  AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDto> cadastrar(
            @RequestBody @Valid AlunoDto dto) {

        return ResponseEntity.status(201).body(alunoService.cadastrar(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("/{id}")
    public ResponseEntity<AlunoResponseDto> atualizar(@PathVariable Integer id,
                                       @RequestBody @Valid AlunoUpdateDto dto) {

        return ResponseEntity.ok(alunoService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDto>> listar() {

        return ResponseEntity.ok(alunoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDto> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                alunoService.buscarPorId(id)
        );
    }
}

