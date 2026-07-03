package com.wictor.controller;

import com.wictor.dto.aluno.AlunoDto;
import com.wictor.dto.aluno.AlunoResponseDto;
import com.wictor.dto.aluno.AlunoUpdateDto;
import com.wictor.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class  AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<AlunoResponseDto> cadastrar(
            @RequestPart("dados") @Valid AlunoDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(alunoService.cadastrar(dto, foto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA') or #id == authentication.principal.id")
    @PatchMapping("/{id}")
    public ResponseEntity<AlunoResponseDto> atualizar(@PathVariable Integer id,
                                       @RequestBody @Valid AlunoUpdateDto dto) {

        return ResponseEntity.ok(alunoService.atualizar(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<AlunoResponseDto>> listar() {

        return ResponseEntity.ok(alunoService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA', 'PROFESSOR') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDto> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                alunoService.buscarPorId(id)
        );
    }
}

