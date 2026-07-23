package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.funcionario.FuncionarioDto;
import com.wictor.dto.funcionario.FuncionarioResponseDto;
import com.wictor.dto.funcionario.FuncionarioUpdateDto;
import com.wictor.service.FuncionarioService;
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
        name = "Funcionários",
        description = """
                Endpoints responsáveis pelo gerenciamento dos funcionários
                da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, consulta e atualização.
                • FUNCIONÁRIO: pode consultar e atualizar apenas seus próprios dados.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Operation(
            summary = "Cadastrar funcionário",
            description = """
                    Realiza o cadastro de um novo funcionário.

                    A requisição deve ser enviada como multipart/form-data,
                    contendo:

                    • dados: informações do funcionário.
                    • foto: imagem de perfil (opcional).

                    Permissão necessária:
                    ADMIN ou GERENTE.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso.",
            content = @Content(schema = @Schema(implementation = FuncionarioResponseDto.class)))
    @ApiResponse(responseCode = "409", description = "Funcionário já cadastrado.")
    @ApiResponse(responseCode = "413", description = "Imagem excede o tamanho permitido.")
    @ApiResponse(responseCode = "422", description = "Formato de imagem inválido.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FuncionarioResponseDto> cadastrar(
            @RequestPart("dados") @Valid FuncionarioDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.status(201).body(funcionarioService.cadastrar(dto, foto));
    }

    @Operation(
            summary = "Atualizar funcionário",
            description = """
                    Atualiza os dados de um funcionário.

                    ADMIN e GERENTE podem atualizar qualquer funcionário.

                    O funcionário pode atualizar apenas seus próprios dados.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = FuncionarioResponseDto.class))
    )
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE') or #funcionarioId == authentication.principal.funcionarioId")
    @PatchMapping("/{funcionarioId}")
    public ResponseEntity<FuncionarioResponseDto> atualizar(
            @Parameter(description = "Identificador único do funcionário.", example = "1")
            @PathVariable Integer funcionarioId,
            @RequestBody @Valid FuncionarioUpdateDto dto) {

        return ResponseEntity.ok(funcionarioService.atualizar(funcionarioId, dto));
    }

    @Operation(
            summary = "Excluir funcionário",
            description = """
                    Remove um funcionário do sistema.

                    Operação exclusiva para administradores.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Funcionário excluído com sucesso.")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{funcionarioId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único do funcionário.", example = "1")
            @PathVariable Integer funcionarioId) {

        funcionarioService.deletar(funcionarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar funcionários",
            description = """
                    Retorna todos os funcionários cadastrados.

                    Disponível para ADMIN e GERENTE.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = FuncionarioResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDto>> listar() {

        return ResponseEntity.ok(funcionarioService.listar());
    }

    @Operation(
            summary = "Buscar funcionário por ID",
            description = """
                    Retorna os dados completos de um funcionário.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Funcionário encontrado com sucesso.",
            content = @Content(schema = @Schema(implementation = FuncionarioResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping("/{funcionarioId}")
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(
            @Parameter(description = "Identificador único do funcionário.", example = "1")
            @PathVariable Integer funcionarioId) {

        return ResponseEntity.ok(funcionarioService.buscarPorId(funcionarioId));
    }
}