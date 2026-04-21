package com.wictor.controller;

import com.wictor.Dto.*;
import com.wictor.Service.FuncionarioService;
import com.wictor.model.Funcionario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid FuncionarioDto dto) {

        Funcionario funcionario = funcionarioService.cadastrar(dto);
        return ResponseEntity.status(201).body(funcionario);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id,
                                       @RequestBody @Valid FuncionarioDto dto) {

        Funcionario funcionario = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        funcionarioService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Funcionário excluído"));
    }

}