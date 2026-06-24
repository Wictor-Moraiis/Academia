package com.wictor.controller;

import com.wictor.dto.aluno.AlunoAdminDto;
import com.wictor.dto.aluno.AlunoDto;
import com.wictor.dto.aluno.AlunoResponseDto;
import com.wictor.dto.aluno.AlunoUpdateDto;
import com.wictor.model.User;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alunos")
public class  AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<AlunoResponseDto> cadastrarAdmin(
            @RequestBody @Valid AlunoAdminDto dto) {

        return ResponseEntity.status(201).body(alunoService.cadastrar(dto));
    }

    @PostMapping("/me")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<AlunoResponseDto> cadastrarMe(
            @RequestBody @Valid AlunoDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(
                alunoService.cadastrarMe(dto, user.getId())
        );
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

