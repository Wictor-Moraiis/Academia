package com.wictor.controller;

import com.wictor.Dto.ExercicioDto;
import com.wictor.Dto.ExercicioUpdateDto;
import com.wictor.Service.ExercicioService;
import com.wictor.model.Exercicio;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    public ExercicioController(ExercicioService exercicioService) {
        this. exercicioService =  exercicioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> cadastrar(
            @RequestPart("dados") @Valid ExercicioDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        Exercicio novo = exercicioService.cadastrar(dto, foto);
        return ResponseEntity.status(201).body(novo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/alterar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> alterar(@PathVariable Integer id,
                                     @RequestPart("dados") @Valid ExercicioUpdateDto dto,
                                     @RequestPart(value = "foto", required = false) MultipartFile foto) {

        Exercicio exercicio = exercicioService.atualizar(id, dto, foto);
        return ResponseEntity.ok(exercicio);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        exercicioService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Exercicio excluído"));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(exercicioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(exercicioService.buscarPorId(id));
    }
}
