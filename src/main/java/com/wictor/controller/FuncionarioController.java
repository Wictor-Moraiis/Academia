package com.wictor.controller;

import com.wictor.dto.funcionario.FuncionarioDto;
import com.wictor.dto.funcionario.FuncionarioResponseDto;
import com.wictor.dto.funcionario.FuncionarioUpdateDto;
import com.wictor.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FuncionarioResponseDto> cadastrar(
            @RequestBody @Valid FuncionarioDto dto) {

        return ResponseEntity.status(201).body(funcionarioService.cadastrar(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
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

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDto>> listar() {
        return ResponseEntity.ok(funcionarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

}