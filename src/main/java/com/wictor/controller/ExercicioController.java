package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.categoria.CategoriaResponseDto;
import com.wictor.dto.exercicio.ExercicioDto;
import com.wictor.dto.exercicio.ExercicioResponseDto;
import com.wictor.dto.exercicio.ExercicioUpdateDto;
import com.wictor.service.ExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
        name = "Exercícios",
        description = """
                Endpoints responsáveis pelo gerenciamento dos exercícios
                cadastrados na academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, consulta e atualização.
                • PROFESSOR: cadastro, consulta e atualização.
                • RECEPCIONISTA: somente consulta.
                • ALUNO: somente consulta.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }

    @Operation(
            summary = "Cadastrar exercício",
            description = """
                    Realiza o cadastro de um novo exercício.

                    A requisição deve ser enviada como multipart/form-data,
                    contendo:

                    • dados: informações do exercício.
                    • foto: imagem demonstrativa do exercício (opcional).

                    Permissão necessária:
                    ADMIN, GERENTE ou PROFESSOR.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Exercício cadastrado com sucesso.",
            content = @Content(schema = @Schema(implementation = ExercicioResponseDto.class)))
    @ApiResponse(responseCode = "409", description = "Exercício já cadastrado.")
    @ApiResponse(responseCode = "413", description = "Imagem excede o tamanho permitido.")
    @ApiResponse(responseCode = "422", description = "Formato de imagem inválido.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExercicioResponseDto> cadastrar(
            @RequestPart("dados") @Valid ExercicioDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(exercicioService.cadastrar(dto, foto));
    }

    @Operation(
            summary = "Atualizar exercício",
            description = """
                    Atualiza os dados de um exercício existente.

                    Permissão necessária:
                    ADMIN, GERENTE ou PROFESSOR.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = ExercicioResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Exercício não encontrado.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','PROFESSOR')")
    @PatchMapping(value = "/{exercicioId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExercicioResponseDto> atualizar(
            @Parameter(description = "Identificador único do exercício.", example = "1")
            @PathVariable Integer exercicioId,
            @RequestPart("dados") @Valid ExercicioUpdateDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.ok(exercicioService.atualizar(exercicioId, dto, foto));
    }

    @Operation(
            summary = "Excluir exercício",
            description = """
                    Remove um exercício do sistema.

                    Esta operação é exclusiva para administradores.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Exercício excluído com sucesso.")
    @ApiResponse(responseCode = "404", description = "Exercício não encontrado.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{exercicioId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único do exercício.", example = "1")
            @PathVariable Integer exercicioId) {

        exercicioService.deletar(exercicioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar exercícios",
            description = """
                    Retorna todos os exercícios cadastrados.

                    Disponível para:
                    ADMIN, GERENTE, RECEPCIONISTA,
                    PROFESSOR e ALUNO.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Lista de exercícios retornada com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = ExercicioResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping
    public ResponseEntity<List<ExercicioResponseDto>> listar() {

        return ResponseEntity.ok(exercicioService.listar());
    }

    @Operation(
            summary = "Buscar exercício por ID",
            description = """
                    Retorna os dados completos de um exercício.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Exercício encontrado com sucesso.",
            content = @Content(schema = @Schema(implementation = ExercicioResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Exercício não encontrado.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO')")
    @GetMapping("/{exercicioId}")
    public ResponseEntity<ExercicioResponseDto> buscarPorId(
            @Parameter(description = "Identificador único do exercício.", example = "1")
            @PathVariable Integer exercicioId) {

        return ResponseEntity.ok(exercicioService.buscarPorId(exercicioId));
    }
}
