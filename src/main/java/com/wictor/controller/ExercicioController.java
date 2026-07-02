package com.wictor.controller;

import com.wictor.dto.exercicio.ExercicioDto;
import com.wictor.dto.exercicio.ExercicioResponseDto;
import com.wictor.dto.exercicio.ExercicioUpdateDto;
import com.wictor.service.ExercicioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService =  exercicioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ExercicioResponseDto> cadastrar(
            @RequestPart("dados") @Valid ExercicioDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(exercicioService.cadastrar(dto,foto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ExercicioResponseDto> atualizar(@PathVariable Integer id,
                                     @RequestPart("dados") @Valid ExercicioUpdateDto dto,
                                     @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.ok(exercicioService.atualizar(id, dto, foto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        exercicioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping
    public ResponseEntity<List<ExercicioResponseDto>> listar() {
        return ResponseEntity.ok(exercicioService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(exercicioService.buscarPorId(id));
    }
}
