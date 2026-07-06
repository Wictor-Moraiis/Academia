package com.wictor.controller;

import com.wictor.dto.funcionario.FuncionarioDto;
import com.wictor.dto.funcionario.FuncionarioResponseDto;
import com.wictor.dto.funcionario.FuncionarioUpdateDto;
import com.wictor.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FuncionarioResponseDto> cadastrar(
            @RequestPart("dados") @Valid FuncionarioDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(funcionarioService.cadastrar(dto, foto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE') or #id == authentication.principal.id")
    @PatchMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> atualizar(@PathVariable Integer id,
                                                            @RequestBody @Valid FuncionarioUpdateDto dto) {

        return ResponseEntity.ok(funcionarioService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDto>> listar() {
        return ResponseEntity.ok(funcionarioService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

}