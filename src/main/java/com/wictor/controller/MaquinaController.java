package com.wictor.controller;

import com.wictor.Dto.MaquinaDto;
import com.wictor.Dto.MaquinaUpdateDto;
import com.wictor.Service.MaquinaService;
import com.wictor.model.Maquina;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService MaquinaService;

    public MaquinaController(MaquinaService MaquinaService) {
        this. MaquinaService =  MaquinaService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid MaquinaDto dto) {

        Maquina novo = MaquinaService.cadastrar(dto);
        return ResponseEntity.status(201).body(novo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@PathVariable Integer id,
                                     @RequestBody @Valid MaquinaUpdateDto dto) {

        Maquina maquina = MaquinaService.atualizar(id, dto);
        return ResponseEntity.ok(maquina);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<?> desativar(@PathVariable Integer id) {

        MaquinaService.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Máquina desativada"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("reativar/{id}")
    public ResponseEntity<?> reativar(@PathVariable Integer id) {
        MaquinaService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Máquina reativada"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        MaquinaService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Maquina excluída"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(MaquinaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Maquina maquina = MaquinaService.buscarPorId(id);
        return ResponseEntity.ok(maquina);
    }
}
